package top.scaleda
package kernel.shell

import kernel.net.RemoteClient
import kernel.net.remote.Empty
import kernel.project.ScaledaProject
import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig, TaskType}
import kernel.shell.command.{CommandDeps, CommandRunner, RemoteCommandDeps}
import kernel.toolchain.executor._
import kernel.toolchain.{Toolchain, ToolchainProfile}
import kernel.utils.{EnvironmentUtils, KernelLogger}
import io.grpc.StatusRuntimeException
import top.scaleda.kernel.toolchain.runner.{ScaledaRunStage, ScaledaRuntime}

import java.io.File
import java.util.Date
import scala.collection.mutable.ArrayBuffer

object ScaledaRun {

  /** Must call before run a task <br/>
    * 1. may do preset <br/>
    * 2. prepare workdir <br/>
    * @param rt runtime
    * @return new runtime
    */
  def preprocess(rt: ScaledaRuntime, writeable: Boolean): ScaledaRuntime = {
    implicit val manifest: ScaledaProject = rt.project
    if (!rt.executor.workingDir.exists() && !rt.executor.workingDir.mkdirs())
      KernelLogger.warn("Cannot create working directory!")
    if (!rt.task.custom.getOrElse(false) && rt.stage == ScaledaRunStage.Prepare) {
      // fetch remote system info
      val remoteInfo =
        if (rt.profile.isRemoteProfile) {
          val (client, shutdown) = RemoteClient(rt.profile.host)
          val remoteInfoReply    = client.getRemoteInfo(Empty.of())
          shutdown()
          Some(remoteInfoReply)
        } else None
      Toolchain.toolchainPresetHandler
        .get(rt.target.toolchain)
        .flatMap(_.handlePreset(rt, remoteInfo, writeable))
        .getOrElse(rt)
        .copy(stage = ScaledaRunStage.PresetDone)
    } else rt
  }

  /** Run a task.
    * @param handler A [[ScaledaRunHandler]] used to redirect output and error
    * @param rt: Whether to run remotely: `rt.profile.host`
    */
  def runTask(
      handler: ScaledaRunHandler,
      rt: ScaledaRuntime
  ): Unit = {
    require(rt.profile.profileName.nonEmpty, "must provide profile before runTask")
    val remoteDeps =
      if (rt.profile.isRemoteProfile && rt.project.projectBase.nonEmpty)
        Some(RemoteCommandDeps(new File(rt.project.projectBase.get), host = rt.profile.host, runId = rt.id))
      else None
    KernelLogger.info(s"runTask workingDir=${rt.projectBase.getAbsoluteFile}")

    val info      = Toolchain.toolchains(rt.target.toolchain)
    val toolchain = info._2(rt.executor)

    if (!EnvironmentUtils.Backup.env.contains("SKIP_EXECUTION")) {
      // generic commands and apply envs
      val commands = toolchain.commands(rt.task)(project = rt.project).map(c => c.copy(envs = rt.extraEnvs.toSeq))
      try CommandRunner.executeLocalOrRemote(remoteDeps, commands, handler)
      catch {
        case e: Throwable =>
          KernelLogger.info("Exception", e, "when executing", commands, "on", remoteDeps)
          throw e
      }
    } else {
      KernelLogger.warn("Skipped run:", rt)
    }
  }

  def runTaskBackground(
      handler: ScaledaRunHandler,
      runtime: ScaledaRuntime,
      daemon: Boolean = true
  ): Thread = {
    val t = new Thread(() => runTask(handler, runtime), s"run-task-${runtime.id}")
    t.setDaemon(daemon)
    t
  }

  private def generateExecutor(
      target: TargetConfig,
      task: TaskConfig,
      profile: ToolchainProfile,
      project: ScaledaProject
  ): Executor = {
    val projectBase    = project.projectBase.get
    val workingDirName = target.name + "-" + task.name
    task.taskType match {
      case TaskType.Simulation =>
        // FIXME: GENERATE TESTBENCH?
        val testbench    = task.findTopModule.get // FIXME: should not get if None, but...
        val workingPlace = new File(new File(projectBase, ".sim"), workingDirName)
        SimulationExecutor(
          workingDir = workingPlace,
          topModule = testbench,
          vcdFile = new File(workingPlace, testbench + "_waveform.vcd"),
          profile = profile
        )
      // ~FIX ME~: constraints collection
      case TaskType.Synthesis | TaskType.Implement | TaskType.Programming =>
        val selectedConstraints = task.getConstraints
        // val singleFiles: Seq[File] = selectedConstraints.flatMap(path => {
        //   val file = new File(projectBase, path)
        //   if (file.exists() && file.isFile) Some(file)
        //   else None
        // })
        val singleFiles = task.getConstraintFiles(project).toSeq
        KernelLogger.debug(
          "selectedConstraints",
          selectedConstraints,
          "task",
          task.constraintPaths,
          "singleFiles",
          singleFiles
        )
        val singleDirs: Seq[File] = selectedConstraints.flatMap(path => {
          val file = new File(projectBase, path)
          if (file.exists() && file.isDirectory) Some(file)
          else None
        })
        task.taskType match {
          case TaskType.Synthesis =>
            SynthesisExecutor(
              workingDir = new File(new File(projectBase, ".synth"), workingDirName),
              topModule = task.findTopModule.get,
              profile = profile,
              constraints = singleFiles,
              constraintsDir = singleDirs
            )
          // }
          // if (task.taskType == TaskType.Implement)
          case TaskType.Implement =>
            ImplementExecutor(
              workingDir = new File(new File(projectBase, ".impl"), workingDirName),
              topModule = task.findTopModule.get,
              profile = profile,
              // in `handlePreset`, constraintsDir will be scanned to add constraints
              // constraints = singleFile.map(Seq(_)).getOrElse(Seq()),
              // constraintsDir = singleDir
              // constraints = task.getConstraintFiles(project).toSeq,
              constraints = singleFiles,
              constraintsDir = singleDirs
            )
          case TaskType.Programming =>
            // else
            ProgrammingExecutor(
              // TODO: should use .prog dir?
              workingDir = new File(new File(projectBase, ".impl"), workingDirName),
              profile = profile,
              topModule = task.findTopModule.get,
              // constraints = singleFile.map(Seq(_)).getOrElse(Seq()),
              constraints = singleFiles,
              constraintsDir = singleDirs
            )
        }
    }
  }

  /** Generate a runtime from names.<br/>
    * May reach remote profiles.
    * @param targetName name of target in config
    * @param taskName name of task in config
    * @param profileName name of profile, local / remote
    * @param profileHost empty if local
    * @return
    */
  def generateRuntimeFromName(
      targetName: String,
      taskName: String,
      profileName: String,
      profileHost: String
  )(implicit project: ScaledaProject): Option[ScaledaRuntime] = {
    val configOptional = ProjectConfig.getConfig
    if (configOptional.isEmpty) {
      KernelLogger.warn("no configure found")
      return None
    }
    val c = configOptional.get
    c.taskByTaskTargetName(taskName, targetName)
      .flatMap(f => {
        val (target, task)                                = f
        var remoteProfiles: Option[Seq[ToolchainProfile]] = None
        val profileHostUse                                = task.host.getOrElse(profileHost)
        KernelLogger.debug(s"profileHostUse: $profileHostUse")
        val profile =
          if (profileHostUse == null || profileHostUse.isEmpty) {
            // Run locally if no host argument provided
            Toolchain
              .profiles()
              .find(p => p.toolchainType == target.toolchain && (p.profileName == profileName || profileName.isEmpty))
          } else {
            // TODO: remote profiles request util
            try {
              val (client, shutdown) = RemoteClient(profileHostUse)
              try {
                remoteProfiles = Some(
                  client
                    .getProfiles(Empty())
                    .profiles
                    .map(p => ToolchainProfile.asRemoteToolchainProfile(p, profileHostUse))
                )
              } finally {
                shutdown()
              }
            } catch {
              case e: StatusRuntimeException =>
                KernelLogger.warn("Cannot load profiles from host", profileHostUse, e)
                return None
            }
            remoteProfiles.get
              .find(p => p.toolchainType == target.toolchain && (p.profileName == profileName || profileName.isEmpty))
          }
        if (profile.isEmpty) {
          KernelLogger.warn("Cannot specify profile! profiles:", Toolchain.profiles())
          None
        } else {
          val runtimeId =
            // NameLegalization.legalization(
            s"${target.toolchain}-${target.name}-${task.name}-${new Date()}"
          // )

          val executor    = ScaledaRun.generateExecutor(target, task, profile.get, project)
          val projectBase = new File(project.projectBase.get)
          val runtime = ScaledaRuntime(
            id = runtimeId,
            target = target,
            task = task,
            profile = profile.get,
            executor = executor,
            projectBase = projectBase,
            project = project
          )

          Some(runtime)
        }
      })
  }
}

trait ScaledaRunHandler {
  def onStdout(data: String): Unit

  def onStderr(data: String): Unit

  /** Invoked when one return value captured, and `finishedAll` == true after all commands done
    * @param returnValue return value
    * @param finishedAll is all commands finished
    * @param meetErrors has errors when executing
    */
  def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit

  /** Return true if handler is stopping this process
    * @return terminating
    */
  def isTerminating: Boolean = false

  /** Invoked before every command call
    * @param command command
    */
  def onShellCommand(command: CommandDeps): Unit = {}

  def onStepDescription(data: String): Unit = {}

  def expectedReturnValue: Int = 0
}

/** RunHandler that logging outputs to [[KernelLogger]]
  */
trait ScaledaRunKernelHandlerWithReturn extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = KernelLogger.info(data)

  override def onStderr(data: String): Unit = KernelLogger.error(data)
}

object ScaledaRunKernelHandler extends ScaledaRunKernelHandlerWithReturn {
  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
    if (meetErrors)
      KernelLogger.warn(s"Command failed, returns $returnValue; finishedAll: $finishedAll, meetErrors: $meetErrors")
    else KernelLogger.info(s"Command done, returns $returnValue; finishedAll: $finishedAll, meetErrors: $meetErrors")
  }
}

object ScaledaRunKernelRemoteHandler extends ScaledaRunKernelHandlerWithReturn {
  override def onStdout(data: String): Unit = KernelLogger.info("[remote]", data)

  override def onStderr(data: String): Unit = KernelLogger.error("[remote]", data)

  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
    if (meetErrors)
      KernelLogger.warn(
        s"Remote command failed, returns $returnValue; finishedAll: $finishedAll, meetErrors: $meetErrors"
      )
    else
      KernelLogger.info(
        s"Remote command done, returns $returnValue; finishedAll: $finishedAll, meetErrors: $meetErrors"
      )
  }
}

/** RunHandler that records return value and outputs to [[returnValues]]
  * @param returnValues optional array to store results
  * @param outputs array to store stdout outputs
  * @param errors array to store stderr outputs
  */
class ScaledaRunHandlerToArray(
    returnValues: Option[ArrayBuffer[Int]],
    outputs: ArrayBuffer[String],
    errors: Option[ArrayBuffer[String]] = None
) extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = outputs.addOne(data)

  override def onStderr(data: String): Unit =
    errors.map(_.addOne(data)).getOrElse(onStdout(data))

  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit =
    returnValues.foreach(_.addOne(returnValue))
}

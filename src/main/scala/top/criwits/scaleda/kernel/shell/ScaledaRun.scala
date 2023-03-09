package top.criwits.scaleda
package kernel.shell

import idea.runner.ScaledaRuntimeInfo
import kernel.net.RemoteClient
import kernel.net.remote.Empty
import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig, TaskType}
import kernel.shell.command.{CommandDeps, CommandRunner, RemoteCommandDeps}
import kernel.toolchain.executor.{Executor, ImplementExecutor, SimulationExecutor, SynthesisExecutor}
import kernel.toolchain.{Toolchain, ToolchainProfile}
import kernel.utils.KernelLogger

import io.grpc.StatusRuntimeException

import java.io.File
import java.time.Instant
import scala.collection.mutable.ArrayBuffer

object ScaledaRun {

  /** Run a task.
    * @param handler A [[ScaledaRunHandler]] used to redirect output and error
    * @param rt: Whether to run remotely: `rt.profile.host`
    */
  def runTask(
      handler: ScaledaRunHandler,
      rt: ScaledaRuntimeInfo
  ): Unit = {
    require(rt.profile.profileName.nonEmpty, "must provide profile before runTask")
    val remoteDeps = if (rt.profile.isRemoteProfile) Some(RemoteCommandDeps(host = rt.profile.host)) else None
    KernelLogger.info(s"runTask workingDir=${rt.workingDir.getAbsoluteFile}")

    val info = Toolchain.toolchains(rt.target.toolchain)
    val rtProcessed =
      if (rt.task.preset) {
        Toolchain.toolchainPresetHandler.get(rt.target.toolchain).map(_.handlePreset(rt)).getOrElse(rt)
      } else rt
    val toolchain = info._2(rt.executor)
    val commands  = toolchain.commands(rtProcessed.task)
    CommandRunner.executeLocalOrRemote(remoteDeps, commands, handler)
  }

  def runTaskBackground(
      handler: ScaledaRunHandler,
      runtime: ScaledaRuntimeInfo,
      daemon: Boolean = true
  ): Thread = {
    val t = new Thread(() => runTask(handler, runtime))
    t.setDaemon(daemon)
    t
  }

  def generateExecutor(
      target: TargetConfig,
      task: TaskConfig,
      profile: ToolchainProfile,
      workingDir: File
  ): Executor = {
    val workingDirName = target.name + "-" + task.name
    task.taskType match {
      case TaskType.Simulation =>
        // FIXME: GENERATE TESTBENCH?
        val testbench    = task.findTopModule.get // FIXME: should not get if None, but...
        val workingPlace = new File(new File(workingDir, ".sim"), workingDirName)
        SimulationExecutor(
          workingDir = workingPlace,
          topModule = testbench,
          vcdFile = new File(workingPlace, testbench + "_waveform.vcd"),
          profile = profile
        )
      case TaskType.Synthesis =>
        SynthesisExecutor(
          workingDir = new File(new File(workingDir, ".synth"), workingDirName),
          topModule = task.findTopModule.get,
          profile = profile
        )
      case TaskType.Implement =>
        ImplementExecutor(
          workingDir = new File(new File(workingDir, ".impl"), workingDirName),
          topModule = task.findTopModule.get,
          profile = profile
        )
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
  ): Option[ScaledaRuntimeInfo] = {
    val configOptional = ProjectConfig.getConfig()
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
        KernelLogger.warn(s"profileHostUse: $profileHostUse")
        val profile =
          if (profileHostUse.isEmpty) {
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
                // TODO: i18n
                KernelLogger.warn("Cannot load profiles form host", profileHostUse, e)
                return None
            }
            remoteProfiles.get
              .find(p => p.toolchainType == target.toolchain && (p.profileName == profileName || profileName.isEmpty))
          }
        if (profile.isEmpty) {
          None
        } else {
          val runtimeId =
            s"${target.toolchain}-${target.name}-${task.name}-${Instant.now()}"

          val workingDir = new File(ProjectConfig.projectBase.get)
          val executor   = ScaledaRun.generateExecutor(target, task, profile.get, workingDir)
          val runtime = ScaledaRuntimeInfo(
            id = runtimeId,
            target = target,
            task = task,
            profile = profile.get,
            executor = executor,
            workingDir = workingDir
          )

          Some(runtime)
        }
      })
  }
}

trait ScaledaRunHandler {
  def onStdout(data: String): Unit

  def onStderr(data: String): Unit

  /** Invoked when one return value captured, and [[finishedAll]] == true after all commands done
    * @param returnValue return value
    * @param finishedAll is all commands finished
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
    // TODO: i18n
    KernelLogger.info(s"command done, returns $returnValue, finishedAll: $finishedAll, meetErrors: $meetErrors")
  }
}

object ScaledaRunKernelRemoteHandler extends ScaledaRunKernelHandlerWithReturn {
  override def onStdout(data: String): Unit = KernelLogger.info("[remote]", data)

  override def onStderr(data: String): Unit = KernelLogger.error("[remote]", data)

  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
    // TODO: i18n
    if (meetErrors) {
      KernelLogger.error("[remote]", s"command error, returns $returnValue")
    } else {
      KernelLogger.info("[remote]", s"command done, returns $returnValue")
    }
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

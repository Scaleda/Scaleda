package top.criwits.scaleda
package kernel.toolchain.impl

import idea.runner.ScaledaRuntimeInfo
import kernel.net.remote.RemoteInfo
import kernel.net.user.ScaledaAuthorizationProvider
import kernel.project.config.TaskType.{Implement, Simulation}
import kernel.project.config.{TargetConfig, TaskConfig, TaskType}
import kernel.shell.ScaledaRunHandlerToArray
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.template.ResourceTemplateRender
import kernel.toolchain.executor.{Executor, ImplementExecutor, SimulationExecutor}
import kernel.toolchain.{Toolchain, ToolchainPresetProvider, ToolchainProfile, ToolchainProfileDetector}
import kernel.utils._

import java.io.File
import scala.async.Async.{async, await}
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global

/** Implementation for EDA toolchain software, Vivado
  *
  * @param executor An [[Executor]] used to hold information like configurations.
  */
class Vivado(executor: Executor) extends Toolchain(executor) with ToolchainProfileDetector {
  import Vivado._
  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  private def getTclFromTask(task: TaskConfig, defaultTcl: String): String = {
    val tclUse = task.tcl.getOrElse(defaultTcl)
    if (task.tcl.isEmpty)
      KernelLogger.warn("did not specify tcl for vivado target, fallback to default", tclUse)
    tclUse
  }

  private def commandDepsForSingleTcl(tclUse: String) = Seq(
    CommandDeps(
      args = Seq(
        getVivadoExec(executor.profile.path),
        "-mode",
        "batch",
        "-source",
        tclUse
      ),
      path = executor.workingDir.getAbsolutePath
    )
  )

  override def simulate(task: TaskConfig) = {
    val tclUse = getTclFromTask(task, "run_sim.tcl")
    commandDepsForSingleTcl(tclUse)
  }

  override def synthesise(task: TaskConfig) = {
    val tclUse = getTclFromTask(task, "run_synth.tcl")
    commandDepsForSingleTcl(tclUse)
  }

  override def implement(task: TaskConfig) = {
    val tclUse = getTclFromTask(task, "run_impl.tcl")
    commandDepsForSingleTcl(tclUse)
  }

  override def programming(task: TaskConfig) = {
    val tclUse = getTclFromTask(task, "run_program.tcl")
    // TODO: Vivado programming
    commandDepsForSingleTcl(tclUse)
  }

  override def detectProfiles = Vivado.detectProfiles
}

object Vivado extends ToolchainProfileDetector with ToolchainPresetProvider {
  val userFriendlyName: String = "Xilinx Vivado"
  val internalID: String       = "vivado"

  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Simulation,
    TaskType.Synthesis,
    TaskType.Implement,
    TaskType.Programming
  )

  def getVivadoExec(path: String): String = new File(
    new File(path),
    "/bin/vivado" + (if (OS.isWindows) ".bat" else "")
  ).getAbsolutePath

  class Verifier(override val toolchainProfile: ToolchainProfile) extends ToolchainProfile.Verifier(toolchainProfile) {
    override def verifyCommandLine: Option[Seq[CommandDeps]] = {
      val vivadoFile = new File(getVivadoExec(toolchainProfile.path))
      if (!vivadoFile.exists()) {
        return None
      }
      Some(Seq(CommandDeps(args = Seq(vivadoFile.getAbsolutePath, "-version"))))
    }

    override def parseVersionInfo(
        returnValues: Seq[Int],
        outputs: Seq[String]
    ): (Boolean, Option[String]) =
      Verifier.parseVersionInfo(returnValues, outputs)
  }

  object Verifier {
    def parseVersionInfo(
        returnValues: Seq[Int],
        outputs: Seq[String]
    ): (Boolean, Option[String]) = {
      if (!returnValues.map(_ == 0).reduce(_ && _)) {
        (false, None)
      } else {
        (true, Some(outputs.head.split("\\n").head)) // should work
      }
    }
  }

  case class TemplateContext(
      top: String, // Top module, if sim == true, then it is testbench; otherwise, it is topmodule
      workDir: String,
      device: String,
      `package`: String,
      speed: String,
      jobs: Int = OS.cpuCount,
      sourceList: Seq[String] = Seq(), // NOTE: simulation top file is EXCLUDED here!
      sim: Boolean,
      testbenchSource: String, // empty if no testbench is given
      ipList: Seq[String] = Seq(),
      xdcList: Seq[String] = Seq(),
      timingReport: Boolean = false,
      vcdFile: String
  )

  class TemplateRenderer(
      executor: Executor,
      targetConfig: TargetConfig,
      taskConfig: TaskConfig
  )(implicit replace: ImplicitPathReplace = NoPathReplace)
      extends ResourceTemplateRender(
        "tcl/vivado",
        executor.workingDir.getAbsolutePath,
        Map(
          "args.tcl.j2"           -> "args.tcl",
          "create_project.tcl.j2" -> "create_project.tcl",
          "run_sim.tcl.j2"        -> "run_sim.tcl",
          "run_synth.tcl.j2"      -> "run_synth.tcl",
          "run_impl.tcl.j2"       -> "run_impl.tcl",
          "run_program.tcl.j2"    -> "run_program.tcl"
        )
      )(replace) {

    override def context: Map[String, Any] = {
      def doSeparatorReplace(src: String): String = src.replace('\\', '/')
      val top =
        taskConfig.findTopModule.get // TODO / FIXME: Exception // TODO: topModule is in executor???
      val sim             = taskConfig.taskType == Simulation
      val topFile         = KernelFileUtils.getModuleFile(top, testbench = sim).get // TODO / FIXME
      val testbenchSource = doSeparatorReplace(topFile.getAbsolutePath)
      val vcdFile =
        if (sim) doSeparatorReplace(executor.asInstanceOf[SimulationExecutor].vcdFile.getAbsolutePath) else ""
      val impl    = taskConfig.taskType == Implement
      val xdcList = if (impl) executor.asInstanceOf[ImplementExecutor].constraints.map(_.getAbsolutePath) else Seq()
      val context = Vivado.TemplateContext(
        top = top,
        workDir = doSeparatorReplace(executor.workingDir.getAbsolutePath),
        device = targetConfig.options.get("device"),     // FIXME
        `package` = targetConfig.options.get("package"), // FIXME
        speed = targetConfig.options.get("speed"),       // FIXME
        sourceList = KernelFileUtils
          .getAllSourceFiles()
          .filter(f => (!sim) || f.getAbsolutePath != topFile.getAbsolutePath)
          .map(p => doSeparatorReplace(p.getAbsolutePath)),
        sim = sim,
        // if sim == false, then this will not be used
        testbenchSource = testbenchSource,
        vcdFile = vcdFile,
        xdcList = xdcList
      )
      Serialization.getCCParams(context)
    }
  }

  override def detectProfiles = async {
    Paths.findExecutableOnPath("vivado") match {
      case Some(vivadoPath) =>
        val vivadoFile = new File(vivadoPath)
        val commands = Seq(
          CommandDeps(args = Seq(vivadoFile.getAbsolutePath, "-version"))
        )
        val outputStrings = ArrayBuffer[String]()
        val returnValues = await(
          CommandRunner.executeAsync(
            commands,
            new ScaledaRunHandlerToArray(None, outputStrings)
          )
        )
        val result =
          Vivado.Verifier.parseVersionInfo(returnValues, outputStrings.toSeq)
        if (result._1) {
          val profile =
            new ToolchainProfile(
              result._2.getOrElse(userFriendlyName),
              internalID
            )
          profile.path = vivadoFile.getAbsolutePath
            .replace("/bin/vivado", "")
            .replace("\\bin\\vivado.bat", "")
          Seq(profile)
        } else Seq()
      case None => Seq()
    }
  }
  ToolchainProfileDetector.registerDetector(this)

  override def handlePreset(rtOld: ScaledaRuntimeInfo, remoteInfo: Option[RemoteInfo]): Option[ScaledaRuntimeInfo] = {
    var rt            = rtOld
    val userTokenBean = ScaledaAuthorizationProvider.loadByHost(rt.profile.host)
    // A local username is required...
    // TODO: Move preset process to server side?
    if (rt.profile.isRemoteProfile) {
      if (userTokenBean.isEmpty || userTokenBean.exists(_.username.isEmpty)) {
        KernelLogger.warn("Cannot apply Vivado preset! Check your user token info, re-login or register")
        return None
      }
    }
    implicit val replace =
      if (rt.profile.isRemoteProfile) {
        require(remoteInfo.nonEmpty, "remote info required before handle preset")
        // this path may not exist on local
        val remoteTargetPath = remoteInfo.get.tempPrefix +
          (if (remoteInfo.get.os.isRemoteOsTypeWindows) "\\" else "/") +
          userTokenBean.get.username
        val pathFrom = rt.workingDir.getAbsolutePath.replace('\\', '/')
        new ImplicitPathReplace(rt.workingDir.getAbsolutePath, remoteTargetPath) {
          override def doReplace(src: String) = {
            KernelLogger.info("Vivado preset replace works", "from", pathFrom, "to", remoteTargetPath)
            super.doReplace(src)
          }
        }
      } else NoPathReplace

    rt.task.taskType match {
      case Implement =>
        // load constraints from dir
        val executor = rt.executor.asInstanceOf[ImplementExecutor]
        executor.constraintsDir
          .flatMap(dir => {
            if (dir.exists() && dir.isDirectory) {
              val constraints = KernelFileUtils.getAllSourceFiles(sourceDir = dir, suffixing = Set("xdc"))
              Some(executor.copy(constraints = executor.constraints ++ constraints, constraintsDir = None))
            } else None
          })
          .foreach(executorNew => rt = rt.copy(executor = executorNew))
      case _ =>
    }
    val templateRenderer = new Vivado.TemplateRenderer(
      executor = rt.executor,
      targetConfig = rt.target,
      taskConfig = rt.task
    )
    templateRenderer.render()
    if (rt.profile.isRemoteProfile) {
      // remove old vivado project if exists and only for remote
      val top = rt.task.findTopModule.getOrElse("NONE")
      val pathsToDelete = Seq(
        s"$top/$top.sim",
        s"$top/$top.cache",
        s"$top/$top.hw",
        s"$top/$top.ip_user_files",
        s"$top/$top.runs"
      )
      pathsToDelete.foreach(f => {
        val file = new File(rt.executor.workingDir, f)
        KernelLogger.info("vivado preset: removing", file, "exists:", file.exists())
        KernelFileUtils.deleteDirectory(file.toPath)
      })
    }
    rt = rt.copy(task = rt.task.copy(tcl = Some(rt.task.taskType match {
      case TaskType.Simulation  => "run_sim.tcl"
      case TaskType.Synthesis   => "run_synth.tcl"
      case TaskType.Implement   => "run_impl.tcl"
      case TaskType.Programming => "run_program.tcl"
    })))
    Some(rt)
  }
}

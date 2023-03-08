package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig, TaskType}
import kernel.shell.ScaledaRunHandlerToArray
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.template.ResourceTemplateRender
import kernel.toolchain.executor.{Executor, SimulationExecutor}
import kernel.toolchain.{Toolchain, ToolchainProfile, ToolchainProfileDetector}
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

object Vivado extends ToolchainProfileDetector {
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
  ) extends ResourceTemplateRender(
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
      ) {
    val config = ProjectConfig.getConfig()

    override def context: Map[String, Any] = config
      .map(config => {
        val top =
          taskConfig.findTopModule.get // TODO / FIXME: Exception // TODO: topModule is in executor???
        val sim     = taskConfig.`type` == "simulation"
        val topFile = KernelFileUtils.getModuleFile(top, testbench = sim).get // TODO / FIXME
        val context = Vivado.TemplateContext(
          top = top,
          workDir = executor.workingDir.getAbsolutePath,
          device = targetConfig.options.get("device"),     // FIXME
          `package` = targetConfig.options.get("package"), // FIXME
          speed = targetConfig.options.get("speed"),       // FIXME
          sourceList = KernelFileUtils
            .getAllSourceFiles()
            .filter(f => (!sim) || f.getAbsolutePath != topFile.getAbsolutePath)
            .map(_.getAbsolutePath.replace('\\', '/')),
          sim = sim,
          testbenchSource = topFile.getAbsolutePath, // if sim == false, then this will not be used
          vcdFile = if (sim) executor.asInstanceOf[SimulationExecutor].vcdFile.getAbsolutePath else ""
        )
        Serialization.getCCParams(context)
      })
      .getOrElse(Map())
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
}

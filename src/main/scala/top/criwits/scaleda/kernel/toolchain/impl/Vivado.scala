package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig}
import kernel.shell.ScaledaRunKernelHandlerWithReturn
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.template.ResourceTemplateRender
import kernel.toolchain.executor.Executor
import kernel.toolchain.impl.Vivado.{getVivadoExec, internalID, userFriendlyName}
import kernel.toolchain.{Toolchain, ToolchainProfile}
import kernel.utils.{KernelFileUtils, KernelLogger, OS, Serialization}

import java.io.File

/**
 * Implementation for EDA toolchain software, Vivado
 *
 * @param executor An [[Executor]] used to hold information like configurations.
 */
class Vivado(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  override def synthesise(task: TaskConfig) =
    Seq(
      CommandDeps(OS.shell(s"${getVivadoExec(executor.profile.path)} -mode batch -source ${task.tcl}"),
        executor.workingDir.getAbsolutePath),
    )

  override def implement(task: TaskConfig) = Seq(
    CommandDeps(OS.shell(s"${getVivadoExec(executor.profile.path)} -mode batch -source ${task.tcl}"),
      executor.workingDir.getAbsolutePath),
  )
}

object Vivado {
  val userFriendlyName: String = "Xilinx Vivado"
  val internalID: String = "vivado"

  def getVivadoExec(path: String): String = new File(new File(path), "/bin/vivado" + (if (OS.isWindows) ".bat" else "")).getAbsolutePath

  class Verifier(override val toolchainProfile: ToolchainProfile) extends ToolchainProfile.Verifier(toolchainProfile) {
    override def verifyCommandLine: Option[Seq[CommandDeps]] = {
      val vivadoFile = new File(getVivadoExec(toolchainProfile.path))
      if (!vivadoFile.exists()) {
        return None
      }
      val cmdLine = OS.shell(s"${vivadoFile.getAbsolutePath} -version")
      Some(Seq(CommandDeps(cmdLine)))
    }

    override def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String]) = {
      if (!returnValues.map(_ == 0).reduce(_ && _)) {
        (false, None)
      } else {
        (true, Some(outputs.head.split("\\n").head)) // should work
      }
    }
  }

  case class TemplateContext
  (top: String, // Top module, if sim == true, then it is testbench; otherwise, it is topmodule
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
   timingReport: Boolean = false)

  class TemplateRenderer
  (executor: Executor, targetConfig: TargetConfig, taskConfig: TaskConfig)
    extends ResourceTemplateRender(
      "tcl/vivado",
      executor.workingDir.getAbsolutePath,
      Map(
        "args.tcl.j2" -> "args.tcl",
        "create_project.tcl.j2" -> "create_project.tcl",
        "run_synth.tcl.j2" -> "run_synth.tcl",
        "run_impl.tcl.j2" -> "run_impl.tcl",
      )) {
    val config = ProjectConfig.getConfig()

    override def context: Map[String, Any] = config.map(config => {
      val top = taskConfig.findTopModule.get // TODO / FIXME: Exception
      val topFile = KernelFileUtils.getModuleFile(top).get // TODO / FIXME
      val sim = taskConfig.`type` == "simulation"
      val context = Vivado.TemplateContext(
        top = top,
        workDir = executor.workingDir.getAbsolutePath,
        device = targetConfig.device,
        `package` = targetConfig.`package`,
        speed = targetConfig.speed,
        sourceList = KernelFileUtils
          .getAllSourceFiles()
          .filter(f => (!sim) || f.getAbsolutePath != topFile.getAbsolutePath)
          .map(_.getAbsolutePath.replace('\\', '/')),
        sim = sim,
        testbenchSource = topFile.getAbsolutePath // if sim == false, then this will not be used
      )
      Serialization.getCCParams(context)
    }).getOrElse(Map())
  }
}

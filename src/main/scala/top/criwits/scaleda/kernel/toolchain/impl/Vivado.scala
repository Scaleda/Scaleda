package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.ProjectConfig
import kernel.project.task.TargetConfig
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

  override def synthesise() = {
    Seq(
      // CommandDeps("ping -c 3 127.0.0.1", executor.workingDir.getAbsolutePath),
      CommandDeps(OS.shell(s"${getVivadoExec(executor.profile.path)} -mode batch -source run_synth.tcl"), executor.workingDir.getAbsolutePath),
    )
  }
}

object Vivado {
  val userFriendlyName: String = "Xilinx Vivado"
  val internalID: String = "vivado"

  def getVivadoExec(path: String): String = new File(path + "/bin/vivado" + (if (OS.isWindows) ".bat" else "")).getAbsolutePath

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
  (top: String,
   topFile: String,
   topSimFile: String,
   // sim: Boolean,
   workDir: String,
   device: String,
   `package`: String,
   speed: Int,
   jobs: Int = OS.cpuCount,
   sourceList: Seq[String] = Seq(),
   ipList: Seq[String] = Seq(),
   xdcList: Seq[String] = Seq(),
   timingReport: Boolean = false)

  class TemplateRenderer
  (executor: Executor, taskConfig: TargetConfig)
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
      val context = Vivado.TemplateContext(
        top = config.topModule,
        topFile = new File(new File(ProjectConfig.projectBase.get), config.topFile).getAbsolutePath,
        // sim = false,
        topSimFile = config.topSimFile,
        workDir = executor.workingDir.getAbsolutePath,
        device = taskConfig.device,
        `package` = taskConfig.`package`,
        speed = taskConfig.speed,
        sourceList = KernelFileUtils
          .getAllSourceFiles(new File(new File(ProjectConfig.projectBase.get).getAbsolutePath, config.source))
          .map(_.getAbsolutePath)
      )
      Serialization.getCCParams(context)
    }).getOrElse(Map())
  }
}

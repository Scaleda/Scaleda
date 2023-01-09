package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.ProjectConfig
import kernel.project.task.TaskConfig
import kernel.template.ResourceTemplateRender
import kernel.toolchain.{Toolchain, ToolchainProfile}
import kernel.toolchain.executor.Executor
import kernel.toolchain.impl.Vivado.{internalID, userFriendlyName}
import kernel.utils.{KernelFileUtils, KernelLogger, OS, Serialization}

import top.criwits.scaleda.kernel.shell.command.{CommandDeps, CommandResponse, CommandRunner}

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
      CommandDeps(s"${executor.profile.path} -mode batch -source run_synth.tcl", executor.workingDir.getAbsolutePath),
    )
  }
}

object Vivado {
  val userFriendlyName: String = "Xilinx Vivado"
  val internalID: String = "vivado"

  def verify(toolchainProfile: ToolchainProfile): (Int, Option[String]) = {
    // Vivado verifier
    val vivadoFile = new File(toolchainProfile.path + "/bin/vivado" + (if (OS.getOSType == OS.Windows) ".bat" else ""))
    KernelLogger.info(s"Vivado path: ${vivadoFile.getAbsolutePath}")

    if (!vivadoFile.exists()) {
      (-1, None)
    }

    val cmdLine = s"${if (OS.getOSType == OS.Windows) "C:\\Windows\\System32\\cmd.exe /c" else "/bin/sh -c"} \"${vivadoFile.getAbsolutePath} -version\""
    KernelLogger.info(s"Vivado cmd line: ${cmdLine}")

    val command = CommandDeps(cmdLine)

    var versionInfo: String = ""
    var returnValue: Int = 0

    CommandRunner.execute(Seq(command), (r, d) => {
      r match {
        case CommandResponse.Stdout | CommandResponse.Stderr => KernelLogger.info( d.asInstanceOf[String] )
        case CommandResponse.Return => returnValue = d.asInstanceOf[Int]
      }
    })

    KernelLogger.info(s"Return val: $returnValue, out: ${versionInfo}")

    if (returnValue == 0) {
      val versionStr = versionInfo.split("\n").head
      (0, Some(versionStr))
    } else {
      (returnValue, None)
    }

  }
}

case class VivadoTemplateContext
(top: String,
 topFile: String,
 topSimFile: String,
 // sim: Boolean,
 workDir: String,
 device: String,
 `package`: String,
 speed: Int,
 jobs: Int = OS.getCpuCount,
 sourceList: Seq[String] = Seq(),
 ipList: Seq[String] = Seq(),
 xdcList: Seq[String] = Seq(),
 timingReport: Boolean = false)

class VivadoTemplateRenderer
(executor: Executor, taskConfig: TaskConfig)
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
    val context = VivadoTemplateContext(
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
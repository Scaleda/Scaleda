package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.ProjectConfig
import kernel.project.task.TaskConfig
import kernel.template.ResourceTemplateRender
import kernel.toolchain.Toolchain
import kernel.toolchain.executor.Executor
import kernel.toolchain.impl.Vivado.{internalID, userFriendlyName}
import kernel.utils.{OS, Serialization}

import top.criwits.scaleda.kernel.shell.command.CommandDeps

/**
 * Implementation for EDA toolchain software, Vivado
 *
 * @param executor An [[Executor]] used to hold information like configurations.
 */
class Vivado(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  override def synthesise() = {
    // ProjectConfig.getConfig().map(config =>
    //   config.tasksByToolchain(getInternalID).flatMap(t => {
    //     CommandDeps(s"${} -help")
    //   }))
    Seq(
      CommandDeps("echo hi"),
      CommandDeps(s"${executor.profile.path} -help"),
    )
  }
}

object Vivado {
  val userFriendlyName: String = "Xilinx Vivado"
  val internalID: String = "vivado"
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
    Map()) {
  val config = ProjectConfig.getConfig()

  override def context: Map[String, Any] = config.map(config => {
    val context = VivadoTemplateContext(
      top = config.topModule,
      topFile = config.topFile,
      // sim = false,
      topSimFile = config.topSimFile,
      workDir = executor.workingDir.getAbsolutePath,
      device = taskConfig.device,
      `package` = taskConfig.`package`,
      speed = taskConfig.speed,
    )
    Serialization.getCCParams(context)
  }).getOrElse(Map())
}
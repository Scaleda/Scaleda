package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.ProjectConfig
import kernel.shell.command.CommandDeps
import kernel.toolchain.Toolchain
import kernel.toolchain.executor.Executor

class IVerilog(executor: Executor) extends Toolchain(executor) {

  import IVerilog._

  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  private val executable = if (executor.profile.path.isEmpty) "iverilog" else executor.profile.path

  override def simulate() = {
    ProjectConfig.getConfig().map(config =>
      config.targets.collectFirst({ case t if t.toolchain == getInternalID => t }))
      .map(task => {
        Seq(
          CommandDeps(s"${executable}")
        )
      }).getOrElse(Seq())
  }
}

object IVerilog {
  val userFriendlyName: String = "Icarus Verilog"
  val internalID: String = "iverilog"
}
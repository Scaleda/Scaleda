package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.ProjectConfig
import kernel.shell.command.CommandDeps
import kernel.toolchain.{Toolchain, ToolchainProfile}
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

  class Verifier(override val toolchainProfile: ToolchainProfile) extends ToolchainProfile.Verifier(toolchainProfile) {
    /**
     * Generate command line(s) used to verify toolchain profile
     *
     * @return One or more than one command line(s)
     */
    override def verifyCommandLine: Option[Seq[CommandDeps]] = ???

    /**
     * Parse toolchain version information from output of command lines of [[verifyCommandLine]]
     *
     * @param returnValues Return values of commands
     * @param outputs      Output strings of commands
     * @return
     */
    override def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String]) = ???
}
}
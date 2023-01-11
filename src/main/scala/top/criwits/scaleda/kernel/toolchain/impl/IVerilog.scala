package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.project.config.ProjectConfig
import kernel.shell.command.CommandDeps
import kernel.toolchain.{Toolchain, ToolchainProfile}
import kernel.toolchain.executor.Executor

import top.criwits.scaleda.kernel.utils.OS

import java.io.File

class IVerilog(executor: Executor) extends Toolchain(executor) {

  import IVerilog._

  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  private val executable = if (executor.profile.path.isEmpty) "iverilog" else executor.profile.path

  override def simulate() = {
    // TODO: ADD $DUMPVCD
    ProjectConfig.getConfig().map(config =>
      config.targets.collectFirst({ case t if t.toolchain == getInternalID => t }))
      .map(target => {
        Seq(
//          CommandDeps(OS.shell(s"${executor.profile.iverilogPath} -o ${task.get.}")) // use shell?
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
    override def verifyCommandLine: Option[Seq[CommandDeps]] = {
      val iverilogFiles: Seq[File] = Seq(
        new File(toolchainProfile.iverilogPath),
        new File(toolchainProfile.iverilogVPIPath),
        new File(toolchainProfile.vvpPath)
      )

      if (!iverilogFiles.map(_.exists()).reduce(_ && _)) {
        return None
      }

      Some(iverilogFiles.map(f => CommandDeps(OS.shell(s"${f.getAbsolutePath} -V"))))
    }

    /**
     * Parse toolchain version information from output of command lines of [[verifyCommandLine]]
     *
     * @param returnValues Return values of commands
     * @param outputs      Output strings of commands
     * @return
     */
    override def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String]) = {
      if (!Seq(
        outputs.exists(_.contains("Icarus Verilog version")),
        outputs.exists(_.contains("iverilog-vpi")),
        outputs.exists(_.contains("Icarus Verilog runtime version")) // FIXME: some kind of tricks
      ).reduce(_ && _)) {
        (false, None)
      } else {
        (true, Some(outputs.filter(_.contains("Icarus Verilog version")).head))
      }

    }
}
}
package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.shell.command.CommandDeps
import kernel.toolchain.executor.Executor
import kernel.toolchain.{Toolchain, ToolchainProfile}
import kernel.utils.{KernelFileUtils, OS}

import top.criwits.scaleda.kernel.project.config.{TaskConfig, TaskType}

import java.io.File

class IVerilog(executor: Executor) extends Toolchain(executor) {

  import IVerilog._

  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  override def simulate(task: TaskConfig) = {
    // TODO: ADD $DUMPVCD
    // TODO: iverilog output file
    // val outputFile = "simulation"
    val outputFile = s"\"${task.topModule}\""
    val sources = KernelFileUtils.getAllSourceFiles()
    Seq(
      CommandDeps(OS.shell(s"${executor.profile.iverilogPath} ${sources.map(_.getAbsolutePath).mkString(" ")} -o $outputFile"),
        executor.workingDir.getAbsolutePath),
      CommandDeps(OS.shell(s"${executor.profile.vvpPath} $outputFile"), executor.workingDir.getAbsolutePath),
    )
  }
}

object IVerilog {
  val userFriendlyName: String = "Icarus Verilog"
  val internalID: String = "iverilog"

  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Simulation
  )

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
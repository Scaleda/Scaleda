package top.criwits.scaleda
package kernel.toolchain.impl

import idea.runner.ScaledaRuntime
import kernel.net.remote.RemoteInfo
import kernel.project.config.{TaskConfig, TaskType}
import kernel.shell.command.CommandDeps
import kernel.toolchain.executor.{Executor, SimulationExecutor}
import kernel.toolchain.{Toolchain, ToolchainPresetProvider, ToolchainProfile}
import kernel.utils.{FileReplaceContext, KernelFileUtils}

import java.io.File

class IVerilog(executor: Executor) extends Toolchain(executor) {

  import IVerilog._

  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  override def simulate(task: TaskConfig) = {
    val simExecutor = executor.asInstanceOf[SimulationExecutor]
    // create working directory
    val workingDir = simExecutor.workingDir
    workingDir.mkdirs()
    assert(workingDir.exists(), f"Cannot create working directory ${workingDir.getAbsolutePath}")

    // should be the same behavior with [[handlePreset]]

    // get testbench info
    val testbench = simExecutor.topModule
    // generate new testbench file
    val newTestbench     = testbench + "_generated"
    val newTestbenchFile = new File(workingDir, newTestbench + ".v")

    val sources = KernelFileUtils.getAllSourceFiles()

    val simExecutorName = testbench + "_iverilog_executor"

    Seq(
      CommandDeps(
        args = Seq(
          executor.profile.iverilogPath,
          "-s",
          testbench,
          "-o",
          simExecutorName,
          newTestbenchFile.getAbsolutePath
        ) ++ sources.map(_.getAbsolutePath),
        path = workingDir.getAbsolutePath,
        description = "Compiling designs"
      ),
      CommandDeps(
        args = Seq(executor.profile.vvpPath, simExecutorName),
        path = workingDir.getAbsolutePath,
        description = "Running simulation"
      )
    )
  }
}

object IVerilog extends ToolchainPresetProvider {
  val userFriendlyName: String = "Icarus Verilog"
  val internalID: String       = "iverilog"

  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Simulation
  )

  class Verifier(override val toolchainProfile: ToolchainProfile) extends ToolchainProfile.Verifier(toolchainProfile) {

    /** Generate command line(s) used to verify toolchain profile
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

      Some(iverilogFiles.map(f => CommandDeps(args = Seq(f.getAbsolutePath, "-V"))))
    }

    /** Parse toolchain version information from output of command lines of [[verifyCommandLine]]
      *
      * @param returnValues Return values of commands
      * @param outputs      Output strings of commands
      * @return
      */
    override def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String]) = {
      if (
        !Seq(
          outputs.exists(_.contains("Icarus Verilog version")),
          outputs.exists(_.contains("iverilog-vpi")),
          outputs.exists(_.contains("Icarus Verilog runtime version")) // FIXME: some kind of tricks
        ).reduce(_ && _)
      ) {
        (false, None)
      } else {
        (true, Some(outputs.filter(_.contains("Icarus Verilog version")).head))
      }

    }
  }

  override def handlePreset(rt: ScaledaRuntime, remoteInfo: Option[RemoteInfo]) = {
    require(rt.task.taskType == TaskType.Simulation)
    val simExecutor = rt.executor.asInstanceOf[SimulationExecutor]
    // get testbench info
    val testbench     = simExecutor.topModule
    val testbenchFile = KernelFileUtils.getModuleFile(testbench, testbench = true).get

    // generate new testbench file
    val newTestbench     = testbench + "_generated"
    val newTestbenchFile = new File(simExecutor.workingDir, newTestbench + ".v")

    // vcd file
    val vcdFile = simExecutor.vcdFile

    KernelFileUtils.insertAfterModuleHead(
      testbenchFile,
      newTestbenchFile,
      testbench,
      s"""
         |initial begin
         |  $$dumpfile(\"${vcdFile.getName}\");
         |  $$dumpvars;
         |end
         |""".stripMargin
    )
    val replaceFiles = rt.context
      .get("replaceFiles")
      .map(_.asInstanceOf[Seq[FileReplaceContext]])
      .getOrElse(Seq()) :+ FileReplaceContext(from = testbenchFile, to = newTestbenchFile, lineOffset = +4)
    val rtWithContext = rt.copy(context = rt.context ++ Map("replaceFiles" -> replaceFiles))
    Some(rtWithContext)
  }
}

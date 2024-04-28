package top.scaleda.kernel.toolchain.impl

import top.scaleda.kernel.project.ScaledaProject
import top.scaleda.kernel.project.config.{TaskConfig, TaskType}
import top.scaleda.kernel.shell.command.CommandDeps
import top.scaleda.kernel.toolchain.executor.{Executor, SimulationExecutor, SynthesisExecutor}
import top.scaleda.kernel.toolchain.impl.PDS.{internalID, userFriendlyName}
import top.scaleda.kernel.toolchain.{Toolchain, ToolchainProfile}
import top.scaleda.kernel.utils.{KernelFileUtils, OS}

import java.io.File

class BSC(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String       = userFriendlyName

  def commonOptions(workingDir: File): Seq[String] = {
    Seq(new File(executor.profile.path, "bsc").getAbsolutePath) ++
      "+RTS -Ksize -RTS -steps-max-intervals 10000000".split(' ') ++
      Seq("-verbose") ++
      Seq(
        "-bdir",
        workingDir.getAbsolutePath,
        "-fdir",
        workingDir.getAbsolutePath,
        "-info-dir",
        workingDir.getAbsolutePath,
        "-simdir",
        workingDir.getAbsolutePath,
        "-vdir",
        workingDir.getAbsolutePath
      )
  }

  override def simulate(task: TaskConfig)(implicit project: ScaledaProject): Seq[CommandDeps] = {
    val simExecutor         = executor.asInstanceOf[SimulationExecutor]
    val workingDir          = simExecutor.workingDir
    val specifiedWorkingDir = task.getWorkingDirectory
    val taskSourceSet       = task.getSourceSet
    val source              = taskSourceSet.map(new File(_)).find(_.isFile)
    val pathOptions = taskSourceSet.toSeq
      .filter(_.nonEmpty)
      .map(f => new File(f))
      .filter(_.isDirectory)
      .flatMap(p => Seq("-p", "+:" + p))
    Seq(
      CommandDeps(
        args = commonOptions(workingDir) ++
          project.projectBase.map(p => Seq("-p", "+:" + p)).getOrElse(Seq()) ++
          Seq("-sim") ++
          task.topModule.map(module => Seq("-g", module)).getOrElse(Seq()) ++
          pathOptions ++
          source.map(file => Seq("-u", file.getAbsolutePath)).getOrElse(Seq()),
        path = workingDir.getAbsolutePath
      ),
      CommandDeps(
        args = commonOptions(workingDir) ++
          project.projectBase.map(p => Seq("-p", "+:" + p)).getOrElse(Seq()) ++
          Seq("-sim") ++
          task.topModule.map(module => Seq("-e", module)).getOrElse(Seq()) ++
          pathOptions ++
          Seq("-o", new File(workingDir, "sim.out").getAbsolutePath),
        path = workingDir.getAbsolutePath
      ),
      CommandDeps(
        args = Seq(new File(workingDir, "sim.out").getAbsolutePath),
        path = specifiedWorkingDir.getAbsolutePath
      )
    )
  }

  override def synthesise(task: TaskConfig)(implicit project: ScaledaProject): Seq[CommandDeps] = {
    val synExecutor   = executor.asInstanceOf[SynthesisExecutor]
    val workingDir    = synExecutor.workingDir
    val taskSourceSet = task.getSourceSet
    val source        = taskSourceSet.map(new File(_)).find(_.isFile)
    Seq(
      CommandDeps(
        args = commonOptions(workingDir) ++
          Seq("-verilog") ++
          project.projectBase.map(p => Seq("-p", "+:" + p)).getOrElse(Seq()) ++
          task.topModule.map(module => Seq("-g", module)).getOrElse(Seq()) ++
          taskSourceSet.toSeq
            .filter(_.nonEmpty)
            .map(f => new File(f))
            .filter(_.isDirectory)
            .flatMap(p => Seq("-p", "+:" + p)) ++
          source.map(file => Seq("-u", file.getAbsolutePath)).getOrElse(Seq()),
        path = workingDir.getAbsolutePath
      )
    )
  }
}

object BSC {
  val userFriendlyName: String = "BSC"
  val internalID: String       = "BSC"
  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Synthesis
  )

  class Verifier(override val toolchainProfile: ToolchainProfile) extends ToolchainProfile.Verifier(toolchainProfile) {

    /** Generate command line(s) used to verify toolchain profile
      *
      * @return One or more than one command line(s)
      */
    override def verifyCommandLine: Option[Seq[CommandDeps]] = {
      Some(Seq(CommandDeps(args = Seq(new File(toolchainProfile.path, "bsc").getAbsolutePath, "-help"))))
    }

    /** Parse toolchain version information from output of command lines of [[verifyCommandLine]]
      *
      * @param returnValues Return values of commands
      * @param outputs      Output strings of commands
      * @return
      */
    override def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String]) = {
      if (returnValues.forall(_ == 0)) {
        (true, Some("Bluespec compiler version TBD"))
      } else {
        (false, None)
      }
    }
  }
}

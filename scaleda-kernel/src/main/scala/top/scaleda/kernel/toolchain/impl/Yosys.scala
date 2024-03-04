package top.scaleda.kernel.toolchain.impl

import top.scaleda.kernel.project.ProjectManifest
import top.scaleda.kernel.project.config.{TaskConfig, TaskType}
import top.scaleda.kernel.shell.command.CommandDeps
import top.scaleda.kernel.toolchain.{Toolchain, ToolchainProfile}
import top.scaleda.kernel.toolchain.executor.{Executor, SynthesisExecutor}
import top.scaleda.kernel.toolchain.impl.PDS.{internalID, userFriendlyName}
import top.scaleda.kernel.utils.{KernelFileUtils, OS}

import java.io.File

class Yosys(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String       = userFriendlyName

  override def synthesise(task: TaskConfig)(implicit manifest: ProjectManifest) = {
    val synExecutor   = executor.asInstanceOf[SynthesisExecutor]
    val workingDir    = synExecutor.workingDir
    val taskSourceSet = task.getSourceSet
    val sources       = KernelFileUtils.getAllSourceFiles(taskSourceSet)
    Seq(
      CommandDeps(
        args = Seq(
          new File(executor.profile.path, "run" + (if (OS.isWindows) ".bat" else ".sh")).getAbsolutePath,
          new File(
            new File(executor.profile.path, "bin"),
            "yosys" + (if (OS.isWindows) ".exe" else "")
          ).getAbsolutePath,
          "-o",
          new File(workingDir.getAbsolutePath, f"${synExecutor.topModule}.edif").getAbsolutePath,
          "-S"
        ) ++ sources.map(_.getAbsolutePath),
        path = workingDir.getAbsolutePath
      )
    )
  }
}

object Yosys {
  val userFriendlyName: String = "Yosys"
  val internalID: String       = "yosys"
  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Synthesis
  )

  class Verifier(override val toolchainProfile: ToolchainProfile) extends ToolchainProfile.Verifier(toolchainProfile) {

    /** Generate command line(s) used to verify toolchain profile
      *
      * @return One or more than one command line(s)
      */
    override def verifyCommandLine: Option[Seq[CommandDeps]] = {
      if (OS.isWindows) {
        Some(
          Seq(
            CommandDeps(args = Seq(new File(toolchainProfile.path, "environment.bat").getAbsolutePath))
            // CommandDeps(args = Seq(new File(new File(toolchainProfile.path, "bin"), "yosys.exe").getAbsolutePath, "-V"))
          )
        )
      } else {
        None
      }
    }

    /** Parse toolchain version information from output of command lines of [[verifyCommandLine]]
      *
      * @param returnValues Return values of commands
      * @param outputs      Output strings of commands
      * @return
      */
    override def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String]) = {
      // if (outputs.exists(_.contains("Yosys"))) {
      //   (true, Some(outputs.find(_.contains("Yosys")).get))
      // } else {
      //   (false, None)
      // }
      if (returnValues.forall(_ == 0)) {
        (true, Some("Yosys version TBD"))
      } else {
        (false, None)
      }
    }
  }
}

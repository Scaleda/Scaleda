package top.scaleda.kernel.toolchain.impl

import top.scaleda.kernel.project.ScaledaProject
import top.scaleda.kernel.project.config.{TaskConfig, TaskType}
import top.scaleda.kernel.shell.command.CommandDeps
import top.scaleda.kernel.toolchain.{Toolchain, ToolchainProfile}
import top.scaleda.kernel.toolchain.executor.{Executor, SynthesisExecutor}
import top.scaleda.kernel.toolchain.impl.PDS.{internalID, userFriendlyName}
import top.scaleda.kernel.utils.{KernelFileUtils, OS}

import java.io.File

class MLFSP(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String       = userFriendlyName

  override def synthesise(task: TaskConfig)(implicit manifest: ScaledaProject): Seq[CommandDeps] = {
    val synExecutor   = executor.asInstanceOf[SynthesisExecutor]
    val workingDir    = synExecutor.workingDir
    val taskSourceSet = task.getSourceSet
    val sources       = KernelFileUtils.getAllSourceFiles(taskSourceSet)
    Seq(
      CommandDeps(
        args = (if (OS.isWindows) {
          Seq(
            new File(executor.profile.path, "run" + (if (OS.isWindows) ".bat" else ".sh")).getAbsolutePath,
            new File(
              new File(executor.profile.path, "bin"),
              "yosys" + (if (OS.isWindows) ".exe" else "")
            ).getAbsolutePath
          )
        }
        else {
          Seq(
            new File(executor.profile.path, "run.sh").getAbsolutePath
          )
        }),
        path = workingDir.getAbsolutePath
      )
    )
  }
}

object MLFSP {
  val userFriendlyName: String = "Machine Learning in FPGA Synthesis and Placement"
  val internalID: String       = "mlfsp"
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
        None
      } else {
        Some(Seq(
          CommandDeps(args = Seq("yosys", "-V")),
        ))
      }
    }

    /** Parse toolchain version information from output of command lines of [[verifyCommandLine]]
     *
     * @param returnValues Return values of commands
     * @param outputs      Output strings of commands
     * @return
     */
    override def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String]) = {
      if (returnValues.forall(_ == 0)) {
        if (OS.isWindows) {
          (true, Some("Yosys version TBD"))
        } else {
          (true, Some(outputs.head.split(" ").take(2).mkString(" ") + " MLFSP"))
        }
      } else {
        (false, None)
      }
    }
  }
}

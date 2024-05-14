package top.scaleda
package kernel.toolchain.executor

import top.scaleda.kernel.toolchain.ToolchainProfile

import java.io.File

case class SynthesisExecutor(
    override val workingDir: File = new File(".synth"),
    override val profile: ToolchainProfile,
    override val topModule: String,
    override val constraints: Seq[File] = Seq(),
    override val constraintsDir: Seq[File] = Seq()
) extends Executor(profile) {

  /** Collect constraints from dirs
    *
    * @param suffixing filter
    * @return
    */
  override def collectConstraintFiles(suffixing: Set[String]): Executor = {
    val executor = super.collectConstraintFiles(suffixing)
    SynthesisExecutor(
      workingDir = executor.workingDir,
      profile = executor.profile,
      topModule = executor.topModule,
      constraints = executor.constraints,
      constraintsDir = executor.constraintsDir
    )
  }
}

package top.scaleda
package kernel.toolchain.executor

import top.scaleda.kernel.toolchain.ToolchainProfile

import java.io.File

case class SimulationExecutor(
    override val workingDir: File = new File(".sim"),
    override val topModule: String,
    vcdFile: File,
    override val profile: ToolchainProfile
) extends Executor(profile) {
  /** Collect constraints from dirs
    *
    * @param suffixing filter
    * @return
    */
  override def collectConstraintFiles(suffixing: Set[String]): Executor = {
    val executor = super.collectConstraintFiles(suffixing)
    SimulationExecutor(
      workingDir = executor.workingDir,
      topModule = executor.topModule,
      vcdFile = vcdFile,
      profile = executor.profile
    )
  }
}

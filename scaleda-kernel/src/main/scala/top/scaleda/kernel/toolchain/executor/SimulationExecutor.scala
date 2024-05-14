package top.scaleda
package kernel.toolchain.executor

import top.scaleda.kernel.toolchain.ToolchainProfile

import java.io.File

case class SimulationExecutor(
    override val workingDir: File = new File(".sim"),
    override val topModule: String,
    vcdFile: File,
    override val profile: ToolchainProfile
) extends Executor(profile)

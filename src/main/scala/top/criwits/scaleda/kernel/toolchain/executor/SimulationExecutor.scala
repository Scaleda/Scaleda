package top.criwits.scaleda
package kernel.toolchain.executor

import kernel.toolchain.ToolchainProfile

import java.io.File

case class SimulationExecutor(
    override val workingDir: File = new File(".sim"),
    topModule: String,
    vcdFile: File,
    override val profile: ToolchainProfile
) extends Executor(profile)

package top.criwits.scaleda
package kernel.toolchain.executor

import top.criwits.scaleda.kernel.toolchain.ToolchainProfile

import java.io.File

case class SimulationExecutor
(override val workingDir: File = new File(".sim"),
 topModule: String,
 override val profile: ToolchainProfile
) extends Executor(profile)

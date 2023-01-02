package top.criwits.scaleda
package kernel.toolchain.executor

import top.criwits.scaleda.kernel.toolchain.ToolchainProfile

import java.io.File

case class SimulationExecutor
(workingDir: File = new File(".sim"),
 topFile: File,
 override val profile: ToolchainProfile
) extends Executor(profile)

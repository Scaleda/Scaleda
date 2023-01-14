package top.criwits.scaleda
package kernel.toolchain.executor

import kernel.toolchain.ToolchainProfile

import java.io.File

case class ImplementExecutor
(override val workingDir: File = new File(".impl"),
 topModule: String,
 override val profile: ToolchainProfile
) extends Executor(profile)

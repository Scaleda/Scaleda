package top.scaleda
package kernel.toolchain.executor

import kernel.toolchain.ToolchainProfile

import java.io.File

case class ProgrammingExecutor
(override val workingDir: File = new File(".impl"),
 override val profile: ToolchainProfile
) extends Executor(profile)

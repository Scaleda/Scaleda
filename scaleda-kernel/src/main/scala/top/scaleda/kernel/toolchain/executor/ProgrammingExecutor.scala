package top.scaleda
package kernel.toolchain.executor

import kernel.toolchain.ToolchainProfile

import java.io.File

case class ProgrammingExecutor(
    override val workingDir: File = new File(".impl"),
    override val profile: ToolchainProfile,
    override val topModule: String = "",
    override val constraints: Seq[File] = Seq(),
    override val constraintsDir: Seq[File] = Seq()
) extends Executor(profile)

// case class ProgrammingExecutor(override val profile: ToolchainProfile) extends Executor(profile)

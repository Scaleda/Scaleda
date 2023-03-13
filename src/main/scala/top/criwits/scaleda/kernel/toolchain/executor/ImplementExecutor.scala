package top.criwits.scaleda
package kernel.toolchain.executor

import kernel.toolchain.ToolchainProfile

import java.io.File

case class ImplementExecutor(
    override val workingDir: File = new File(".impl"),
    override val profile: ToolchainProfile,
    topModule: String,
    constraints: Seq[File] = Seq(),
    constraintsDir: Option[File] = None
) extends Executor(profile)

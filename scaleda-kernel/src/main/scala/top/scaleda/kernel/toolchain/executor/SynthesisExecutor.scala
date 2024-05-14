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
) extends Executor(profile)

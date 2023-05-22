package top.scaleda
package kernel.toolchain.executor

import top.scaleda.kernel.toolchain.ToolchainProfile

import java.io.File

case class SynthesisExecutor
(override val workingDir: File = new File(".synth"),
 topModule: String,
 override val profile: ToolchainProfile
) extends Executor(profile)

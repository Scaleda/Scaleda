package top.criwits.scaleda
package kernel.toolchain.executor

import top.criwits.scaleda.kernel.toolchain.ToolchainProfile

import java.io.File

case class SynthesisExecutor
(override val workingDir: File = new File(".synth"),
 topFile: File,
 override val profile: ToolchainProfile
) extends Executor(profile)

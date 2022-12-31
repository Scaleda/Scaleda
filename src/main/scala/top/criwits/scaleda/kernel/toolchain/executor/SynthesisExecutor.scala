package top.criwits.scaleda
package kernel.toolchain.executor

import java.io.File

case class SynthesisExecutor
(workingDir: File = new File(".synth"),
 topFile: File
) extends Executor

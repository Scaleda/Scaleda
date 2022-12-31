package top.criwits.scaleda
package kernel.toolchain.executor

import java.io.File

case class SimulationExecutor
(workingDir: File = new File(".sim"),
 topFile: File) extends Executor

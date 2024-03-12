package top.scaleda
package kernel.toolchain.impl

import kernel.toolchain.executor.{Executor, SimulationExecutor}

import top.scaleda.kernel.project.config.TaskType
import top.scaleda.kernel.toolchain.{Toolchain, ToolchainProfile}
import top.scaleda.kernel.toolchain.impl.Verilator.{internalID, userFriendlyName}

import java.io.File

class Verilator(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object Verilator {
  val userFriendlyName: String = "Verilator"
  val internalID: String = "verilator"

  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Simulation
  )

}



package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.simulation.Simulator
import kernel.toolchain.executor.{Executor, SimulationExecutor}

import top.criwits.scaleda.kernel.toolchain.Toolchain

class Verilator(executor: Executor) extends Toolchain(executor) {
  override val userFriendlyName: String = "Verilator"
  override val internalID: String = "verilator"
}

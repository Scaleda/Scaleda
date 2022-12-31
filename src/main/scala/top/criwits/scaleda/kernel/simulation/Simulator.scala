package top.criwits.scaleda
package kernel.simulation

import top.criwits.scaleda.kernel.toolchain.executor.SimulationExecutor
import top.criwits.scaleda.kernel.toolchain.impl.{IVerilog, Verilator}

/**
 * Basic Simulator
 *
 * @param config config
 */
abstract class Simulator(val config: SimulationExecutor) {
  /**
   * Run simulation by config
   *
   * @return simulate return value
   */
  def simulate(): Int
}

object Simulator {
  val simulators = Map(
    "iverilog" -> ((config: SimulationExecutor) => new IVerilog(config)),
    "verilator" -> ((config: SimulationExecutor) => new Verilator(config)),
  )
}

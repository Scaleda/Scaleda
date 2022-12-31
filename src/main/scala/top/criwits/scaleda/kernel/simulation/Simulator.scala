package top.criwits.scaleda
package kernel.simulation

import top.criwits.scaleda.kernel.simulation.iverilog.IVerilog
import top.criwits.scaleda.kernel.simulation.verilator.Verilator

/**
 * Basic Simulator
 *
 * @param config config
 */
abstract class Simulator(val config: SimulationConfig) {
  /**
   * Run simulation by config
   *
   * @return simulate return value
   */
  def simulate(): Int
}

object Simulator {
  val simulators = Map(
    "iverilog" -> ((config: SimulationConfig) => new IVerilog(config)),
    "verilator" -> ((config: SimulationConfig) => new Verilator(config)),
  )
}

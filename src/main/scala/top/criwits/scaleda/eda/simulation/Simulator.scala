package top.criwits.scaleda
package eda.simulation

import eda.simulation.iverilog.IVerilog
import eda.simulation.verilator.Verilator

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

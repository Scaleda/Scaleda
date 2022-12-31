package top.criwits.scaleda
package kernel.simulation.iverilog

import top.criwits.scaleda.kernel.simulation.{SimulationConfig, Simulator}

class IVerilog(config: SimulationConfig) extends Simulator(config: SimulationConfig) {
  /**
   * Run simulation by config
   *
   * @return simulate return value
   */
  override def simulate(): Int = ???
}

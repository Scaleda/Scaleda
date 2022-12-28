package top.criwits.scaleda
package eda.simulation

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

package top.criwits.scaleda
package kernel.toolchain

import top.criwits.scaleda.kernel.toolchain.executor.Executor
import top.criwits.scaleda.kernel.toolchain.impl.{IVerilog, PDS, Quartus, Verilator, Vivado}

/**
 * A [[Toolchain]] is an external software used to perform simulation, synthesis and implementation.
 * Applied toolchains, like [[Vivado]] or [[Quartus]], extend this abstract base class.
 * @param executor An [[Executor]] used to hold information like configurations.
 */
abstract class Toolchain(val executor: Executor) {
  val userFriendlyName: String
  val internalID: String

  def simulate(): Unit = ???
  def synthesise(): Unit = ???
  def implement(): Unit = ???

}

object Toolchain {
  val toolchains: Map[String, Executor => Toolchain] = Map(
    "vivado" -> ((executor: Executor) => new Vivado(executor)),
    "quartus" -> ((executor: Executor) => new Quartus(executor)),
    "pds" -> ((executor: Executor) => new PDS(executor)),
    "verilator" -> ((executor: Executor) => new Verilator(executor)),
    "iverilog" -> ((executor: Executor) => new IVerilog(executor)),
  )

}
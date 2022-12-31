package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.toolchain.Toolchain

import top.criwits.scaleda.kernel.toolchain.executor.Executor


class Vivado(executor: Executor) extends Toolchain(executor) {
  override val userFriendlyName: String = "Xilinx Vivado"
  override val internalID: String = "vivado"
}

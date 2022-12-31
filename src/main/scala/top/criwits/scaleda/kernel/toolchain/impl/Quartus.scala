package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.toolchain.Toolchain

import top.criwits.scaleda.kernel.toolchain.executor.Executor


class Quartus(executor: Executor) extends Toolchain(executor: Executor){
  override val userFriendlyName: String = "Intel Quartus"
  override val internalID: String = "quartus"
}

package top.criwits.scaleda
package kernel.toolchain.impl

import top.criwits.scaleda.kernel.toolchain.Toolchain
import top.criwits.scaleda.kernel.toolchain.executor.Executor

class PDS(executor: Executor) extends Toolchain(executor: Executor) {
  override val userFriendlyName: String = "Pango PDS"
  override val internalID: String = "pds"
}

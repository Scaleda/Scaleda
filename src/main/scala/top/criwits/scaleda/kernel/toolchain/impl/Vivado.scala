package top.criwits.scaleda
package kernel.toolchain.impl

import top.criwits.scaleda.kernel.toolchain.ToolchainType

class Vivado extends ToolchainType {
  override val userFriendName: String = "Xilinx Vivado"
  override val internalID: String = "vivado"
}

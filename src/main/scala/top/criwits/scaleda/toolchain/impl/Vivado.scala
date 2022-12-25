package top.criwits.scaleda
package toolchain.impl

import toolchain.ToolchainType

class Vivado extends ToolchainType {
  override val userFriendName: String = "Xilinx Vivado"
  override val internalID: String = "vivado"
}

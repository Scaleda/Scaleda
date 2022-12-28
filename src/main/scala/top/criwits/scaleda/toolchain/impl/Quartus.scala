package top.criwits.scaleda
package toolchain.impl

import toolchain.ToolchainType

class Quartus extends ToolchainType{
  override val userFriendName: String = "Intel Quartus"
  override val internalID: String = "quartus"
}

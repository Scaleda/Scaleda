package top.criwits.scaleda
package kernel.toolchain.impl

import top.criwits.scaleda.kernel.toolchain.ToolchainType

class Quartus extends ToolchainType{
  override val userFriendName: String = "Intel Quartus"
  override val internalID: String = "quartus"
}

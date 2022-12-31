package top.criwits.scaleda
package kernel.toolchain

import top.criwits.scaleda.kernel.toolchain.impl.{PDS, Quartus, Vivado}

abstract class ToolchainType {
  val userFriendName: String
  val internalID: String

  // TODO: Add EDA / toolchain specified things here... generator? ...
}

object ToolchainType {
  val toolchainList: Array[(String, Class[_ <: ToolchainType])] = Array(
    ("vivado", classOf[Vivado]),
    ("quartus", classOf[Quartus]),
    ("pds", classOf[PDS])
  )


}
package top.criwits.scaleda
package toolchain

abstract class ToolchainType {
  val userFriendName: String
  val internalID: String

  // TODO: Add EDA / toolchain specified things here... generator? ...
}

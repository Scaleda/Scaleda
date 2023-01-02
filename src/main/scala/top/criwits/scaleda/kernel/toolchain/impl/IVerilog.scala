package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.toolchain.executor.Executor
import kernel.toolchain.{Toolchain, ToolchainProfile}

import top.criwits.scaleda.kernel.toolchain.impl.IVerilog.{internalID, userFriendlyName}

class IVerilog(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object IVerilog {
  val userFriendlyName: String = "Icarus Verilog"
  val internalID: String = "iverilog"

  case class Profile(
                      override val profileName: String,
                      path: String
                    ) extends ToolchainProfile(internalID, profileName)
}
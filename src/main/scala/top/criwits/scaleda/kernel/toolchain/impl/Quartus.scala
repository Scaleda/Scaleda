package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.toolchain.{Toolchain, ToolchainProfile}

import top.criwits.scaleda.kernel.toolchain.executor.Executor
import top.criwits.scaleda.kernel.toolchain.impl.Quartus.{internalID, userFriendlyName}

import java.io.File


class Quartus(executor: Executor) extends Toolchain(executor){
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object Quartus {
  val userFriendlyName: String = "Intel Quartus"
  val internalID: String = "quartus"

}
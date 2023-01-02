package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.toolchain.{Toolchain, ToolchainProfile}

import top.criwits.scaleda.kernel.toolchain.executor.Executor
import top.criwits.scaleda.kernel.toolchain.impl.Vivado.{internalID, userFriendlyName}

import java.io.File

/**
 * Implementation for EDA toolchain software, Vivado
 * @param executor An [[Executor]] used to hold information like configurations.
 */
class Vivado(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object Vivado {
  val userFriendlyName: String = "Xilinx Vivado"
  val internalID: String = "vivado"

  case class Profile(
                    override val profileName: String,
                    path: String
                    ) extends ToolchainProfile(internalID, profileName)
}
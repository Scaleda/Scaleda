package top.criwits.scaleda
package kernel.toolchain.impl

import top.criwits.scaleda.kernel.toolchain.{Toolchain, ToolchainProfile}
import top.criwits.scaleda.kernel.toolchain.executor.Executor
import top.criwits.scaleda.kernel.toolchain.impl.PDS.{internalID, userFriendlyName}

class PDS(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object PDS {
  val userFriendlyName: String = "Pango PDS"
  val internalID: String = "pds"

  case class Profile(
                      override val profileName: String,
                      path: String
                    ) extends ToolchainProfile(internalID, profileName)
}
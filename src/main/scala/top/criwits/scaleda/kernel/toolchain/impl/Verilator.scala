package top.criwits.scaleda
package kernel.toolchain.impl

import kernel.toolchain.executor.{Executor, SimulationExecutor}

import top.criwits.scaleda.kernel.toolchain.{Toolchain, ToolchainProfile}
import top.criwits.scaleda.kernel.toolchain.impl.Verilator.{internalID, userFriendlyName}

import java.io.File

class Verilator(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object Verilator {
  val userFriendlyName: String = "Verilator"
  val internalID: String = "verilator"

  case class Profile(
                      override val profileName: String,
                      verilatorPath: String,
                      buildToolPath: String
                    ) extends ToolchainProfile(internalID, profileName)
}



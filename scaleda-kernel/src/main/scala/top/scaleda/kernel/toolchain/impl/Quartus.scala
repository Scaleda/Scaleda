package top.scaleda
package kernel.toolchain.impl

import kernel.toolchain.{Toolchain, ToolchainProfile}

import top.scaleda.kernel.project.config.TaskType
import top.scaleda.kernel.toolchain.executor.Executor
import top.scaleda.kernel.toolchain.impl.Quartus.{internalID, userFriendlyName}

import java.io.File


class Quartus(executor: Executor) extends Toolchain(executor){
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object Quartus {
  val userFriendlyName: String = "Intel Quartus"
  val internalID: String = "quartus"

  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Simulation,
    TaskType.Synthesis,
    TaskType.Implement
  )

}
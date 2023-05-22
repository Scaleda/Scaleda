package top.scaleda
package kernel.toolchain.impl

import top.scaleda.kernel.project.config.TaskType
import top.scaleda.kernel.toolchain.{Toolchain, ToolchainProfile}
import top.scaleda.kernel.toolchain.executor.Executor
import top.scaleda.kernel.toolchain.impl.PDS.{internalID, userFriendlyName}

class PDS(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = internalID
  override def getName: String = userFriendlyName
}

object PDS {
  val userFriendlyName: String = "Pango PDS"
  val internalID: String = "pds"
  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Simulation,
    TaskType.Synthesis,
    TaskType.Implement
  )


}
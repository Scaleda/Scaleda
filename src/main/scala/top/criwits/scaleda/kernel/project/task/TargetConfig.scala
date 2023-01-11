package top.criwits.scaleda
package kernel.project.task

import kernel.utils.HasDefault

case class TargetConfig(
    name: String = "",
    toolchain: String = "",
    device: String = "",
    `package`: String = "",
    speed: Int = 1,
    tasks: Array[TaskConfig] = Array()
) {
  def getPackage = `package`
}

object TargetConfig extends HasDefault[TargetConfig] {
  override def getDefault: TargetConfig = TargetConfig()
}

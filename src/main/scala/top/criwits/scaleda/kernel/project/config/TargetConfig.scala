package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import top.criwits.scaleda.kernel.utils.HasDefault

@JsonInclude(Include.NON_EMPTY)
case class TargetConfig(
    name: String = "",
    toolchain: String = "",
    topModule: String = "",
    device: String = "",
    `package`: String = "",
    speed: Int = 1,
    tasks: Array[TaskConfig] = Array()
) extends ConfigNode() {
  def getPackage = `package`
}

object TargetConfig extends HasDefault[TargetConfig] {
  override def getDefault: TargetConfig = TargetConfig()
}

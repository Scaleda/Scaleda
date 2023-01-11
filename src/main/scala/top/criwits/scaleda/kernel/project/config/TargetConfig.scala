package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_EMPTY)
case class TargetConfig(
    name: String = "",
    toolchain: String = "",
    device: String = "",
    `package`: String = "",
    speed: Int = 1,
    tasks: Array[TaskConfig] = Array()
) extends ConfigNode {
  def getPackage = `package`
}

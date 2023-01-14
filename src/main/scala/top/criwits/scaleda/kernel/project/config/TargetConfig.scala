package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import top.criwits.scaleda.kernel.utils.HasDefault

/**
 * Case class for config of a target
 * @param name Config name
 * @param toolchain Toolchain type of this target
 * @param topModule Top module name of this target; can be inherited if is [[None]]
 * @param options Extra options for this target, e.g. part number (xc7a100tfgg484-1) for Xilinx Vivado
 * @param tasks Tasks under this target
 */
@JsonInclude(Include.NON_EMPTY)
case class TargetConfig(
    name: String = "",
    toolchain: String = "",
    topModule: Option[String] = None,
    options: Option[Map[String, String]] = None,
    tasks: Array[TaskConfig] = Array()
) extends ConfigNode()


object TargetConfig extends HasDefault[TargetConfig] {
  override def getDefault: TargetConfig = TargetConfig()
}

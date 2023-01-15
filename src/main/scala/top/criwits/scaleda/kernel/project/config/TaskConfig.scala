package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}
import com.fasterxml.jackson.annotation.JsonInclude.Include
import top.criwits.scaleda.kernel.utils.HasDefault

/**
 * Case class for config of task
 * @param name Task name, shown in right panel & config menu
 * @param `type` 'simulation', 'synthesis' or 'implementation'
 * @param topModule Top module, if [[None]] then inherited
 * @param preset Use built-in execution policy
 * @param tcl Path to Tcl script
 */
@JsonInclude(Include.NON_EMPTY)
case class TaskConfig(
    name: String = "",
    `type`: String = "",
    topModule: Option[String] = None,
    preset: Boolean = true,
    tcl: Option[String] = None,
) extends ConfigNode() {

  def taskType = `type` match {
    case "simulation"     => TaskType.Simulation
    case "synthesis"      => TaskType.Synthesis
    case "implementation" => TaskType.Implement
    case _                => ??? // FIXME: A better way?
  }

  def taskString = `type`
}

object TaskType extends Enumeration {
  val Simulation, Synthesis, Implement = Value
}

object TaskConfig extends HasDefault[TaskConfig] {
  override def getDefault: TaskConfig = TaskConfig()
}

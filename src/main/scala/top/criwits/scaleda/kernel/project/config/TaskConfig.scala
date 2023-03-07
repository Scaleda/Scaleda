package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}
import com.fasterxml.jackson.annotation.JsonInclude.Include
import top.criwits.scaleda.kernel.project.config.TaskConfig.taskTypeList
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
    host: Option[String] = None,
) extends ConfigNode() {

  def taskType = taskTypeList(`type`)._2

  def taskString = `type`
}

object TaskType extends Enumeration {
  val Simulation, Synthesis, Implement, Programming = Value
}

object TaskConfig extends HasDefault[TaskConfig] {
  override def getDefault: TaskConfig = TaskConfig()

  val taskTypeList: Map[String, (String, TaskType.Value)] = Map(
    "simulation" -> ("Simulation", TaskType.Simulation),
    "synthesis" -> ("Synthesis", TaskType.Synthesis),
    "implementation" -> ("Implementation", TaskType.Implement)
  )
}

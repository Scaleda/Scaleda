package top.criwits.scaleda
package kernel.project.task

import kernel.utils.HasDefault

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}

@JsonInclude(Include.NON_EMPTY)
case class TaskConfig(
    name: String = "",
    `type`: String = "",
    tcl: String = "",
    preset: Boolean = false
) {
  @JsonIgnore
  def getType = `type` match {
    case "simulation"     => TaskType.Simulation
    case "synthesis"      => TaskType.Synthesis
    case "implementation" => TaskType.Implement
    case _                => ???
  }
  @JsonIgnore
  def getTypeString = `type`
}

object TaskType extends Enumeration {
  val Simulation, Synthesis, Implement = Value
}

object TaskConfig extends HasDefault[TaskConfig] {
  override def getDefault: TaskConfig = TaskConfig()
}

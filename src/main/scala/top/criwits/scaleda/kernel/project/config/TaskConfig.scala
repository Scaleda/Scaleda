package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_EMPTY)
case class TaskConfig(
    name: String = "",
    `type`: String = "",
    topModule: String = "",
    tcl: String = "",
    preset: Boolean = false,
) extends ConfigNode() {
  @JsonIgnore
  def getTaskType = `type` match {
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

package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_EMPTY)
case class TaskConfig(
    name: String = "",
    `type`: String = "",
    tcl: String = "",
    preset: Boolean = false
) extends ConfigNode {
  def getType = `type` match {
    case "simulation"     => TaskType.Simulation
    case "synthesis"      => TaskType.Synthesis
    case "implementation" => TaskType.Implement
    case _                => ???
  }
  def getTypeString = `type`
}

object TaskType extends Enumeration {
  val Simulation, Synthesis, Implement = Value
}

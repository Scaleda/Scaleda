package top.criwits.scaleda
package kernel.project.task

case class TaskConfig(
    name: String = "",
    `type`: String = "",
    tcl: String = "",
    preset: Boolean = false
) {
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

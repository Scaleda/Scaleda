package top.criwits.scaleda
package kernel.project.task

case class TargetConfig
(name: String,
 `type`: String,
 tcl: String,
 preset: Boolean = false) {
  def getType = `type` match {
    case "simulation" => TargetType.Simulation
    case "synthesis" => TargetType.Synthesis
    case "implementation" => TargetType.Implement
    case _ => ???
  }
}

object TargetType extends Enumeration {
  val Simulation, Synthesis, Implement = Value
}
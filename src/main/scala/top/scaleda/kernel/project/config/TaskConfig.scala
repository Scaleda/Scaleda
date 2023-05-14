package top.scaleda
package kernel.project.config

import idea.windows.tasks.ip.IPInstance
import kernel.project.config.TaskConfig.taskTypeList
import kernel.utils.HasDefault

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

/** Case class for config of task
  * @param name Task name, shown in right panel & config menu
  * @param `type` 'simulation', 'synthesis', 'implementation' or 'programming'
  * @param topModule Top module, if [[None]] then inherited
  * @param custom Use custom execution policy (tcl)
  * @param tcl Path to Tcl script
  * @param constraints Path to constraints file or directory, if [[None]] then inherited
  */
@JsonInclude(Include.NON_EMPTY)
case class TaskConfig(
    name: String = "",
    `type`: String = "simulation", //tricky
    topModule: Option[String] = None,
    constraints: Option[String] = None,
    // FIXME: this field always dumped...
    custom: Boolean = false,
    tcl: Option[String] = None,
    // force to use remote host
    host: Option[String] = None
    // override val source: String = "",
    // override val sources: Seq[String] = Seq(),
    // override val test: String = "",
    // override val tests: Seq[String] = Seq(),
    // override val ipFiles: Seq[String] = Seq(),
    // override val ipPaths: Seq[String] = Seq(),
    // override val ips: Seq[IPInstance] = Seq()
) extends ConfigNode() {
  override val source: String       = ""
  override val sources: Seq[String] = Seq()
  override val test: String         = ""
  override val tests: Seq[String]   = Seq()
  override val ipFiles: Seq[String] = Seq()
  override val ipPaths: Seq[String] = Seq()
  override val ips: Seq[IPInstance] = Seq()

  def taskType = taskTypeList(`type`)._2

  def taskString = `type`
}

object TaskType extends Enumeration {
  val Simulation, Synthesis, Implement, Programming = Value
}

object TaskConfig extends HasDefault[TaskConfig] {
  override def getDefault: TaskConfig = TaskConfig()

  val taskTypeList: Map[String, (String, TaskType.Value)] = Map( // TODO: extract to bundle
    "simulation"     -> ("Simulation", TaskType.Simulation),
    "synthesis"      -> ("Synthesis", TaskType.Synthesis),
    "implementation" -> ("Implementation", TaskType.Implement),
    "programming"    -> ("Programming", TaskType.Programming)
  )
}

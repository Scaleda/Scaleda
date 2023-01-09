package top.criwits.scaleda
package kernel.project.config

import kernel.project.task.{TaskConfig, TargetConfig}
import kernel.utils.{JsonHelper, KernelLogger, YAMLHelper}

import java.io.File

case class ProjectConfig
(
  name: String = "default-project",
  `type`: String = "rtl",
  source: String = "src/",
  topModule: String = "",
  topFile: String,
  topSimFile: String,
  targets: Array[TargetConfig]
) {
  def targetsWithSim = targets.filter(t => t.tasks.exists(t => t.`type` == "simulation"))

  def targetsWithSynth = targets.filter(t => t.tasks.exists(t => t.`type` == "synthesis"))

  def targetsWithImpl = targets.filter(t => t.tasks.exists(t => t.`type` == "implementation"))

  def targetsByToolchain(toolchain: String) = targets.filter(_.toolchain == toolchain)

  def taskNames = targets.flatMap(_.tasks).map(_.name)

  def taskByName(name: String): Option[(TargetConfig, TaskConfig)] = {
    val r = targets.map(task => (task, task.tasks))
      .filter(f => f._2.exists(t => t.name == name))
      .map(f => (f._1, f._2.reduceOption((a, b) => if (a.name == name) a else b)))
    if (r.isEmpty) return None
    val h = r.head
    if (h._2.isEmpty) return None
    Some((h._1, h._2.get))
  }

}

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"
  var projectBase: Option[String] = None
  var configFile: Option[String] = None

  def getConfig(path: Option[String] = configFile): Option[ProjectConfig] = {
    path match {
      case Some(p) =>
        val config = YAMLHelper(new File(p), classOf[ProjectConfig])
        KernelLogger.info(s"Loaded project config ${JsonHelper(config)}")
        Some(config)
      case None => None
    }
  }
}

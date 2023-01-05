package top.criwits.scaleda
package kernel.project.config

import kernel.utils.{KernelLogger, YAMLHelper}

import java.io.File

case class ProjectConfig
(
  name: String = "default-project",
  `type`: String = "rtl",
  source: String = "src/",
  topFile: String,
  topSimFile: String,
  tasks: Array[TaskConfig]
) {
  def tasksSim = tasks.filter(t => t.`type` == "simulation")

  def tasksSynth = tasks.filter(t => t.`type` == "synthesis")

  def tasksImpl = tasks.filter(t => t.`type` == "implementation")
}

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"
  var configFile: Option[String] = None

  def getConfig(path: Option[String] = configFile): Option[ProjectConfig] = {
    path match {
      case Some(p) =>
        val config = YAMLHelper(new File(p), classOf[ProjectConfig])
        KernelLogger.info(s"Loaded project config ${config.name}")
        Some(config)
      case None => None
    }
  }
}

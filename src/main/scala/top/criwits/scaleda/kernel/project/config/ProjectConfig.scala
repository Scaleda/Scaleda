package top.criwits.scaleda
package kernel.project.config

import kernel.utils.YAMLHelper

import java.io.File

case class ProjectConfig
(
  name: String = "default-project",
  `type`: String = "rtl",
  src: String = ""
)

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"
  var config: Option[ProjectConfig] = None

  def loadConfig(path: File): ProjectConfig = YAMLHelper(path, classOf[ProjectConfig])

  def setConfig(path: File) = {
    val c = loadConfig(path)
    config = Some(c)
  }
}

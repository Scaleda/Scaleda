package top.criwits.scaleda
package kernel.project.config

import java.io.File

case class ProjectConfig
(
  name: String = "default-project",
  `type`: String = "rtl",
  src: String = ""
)

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"

  def loadConfig(path: String): ProjectConfig = YamlBuilder(new File(path), classOf[ProjectConfig])
}

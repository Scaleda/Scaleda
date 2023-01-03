package top.criwits.scaleda
package kernel.project.config

import top.criwits.scaleda.kernel.utils.YAMLHelper

import java.io.File

case class ProjectConfig
(
  name: String = "default-project",
  `type`: String = "rtl",
  src: String = ""
)

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"

  def loadConfig(path: String): ProjectConfig = YAMLHelper(new File(path), classOf[ProjectConfig])
}

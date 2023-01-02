package top.criwits.scaleda
package kernel.project.config

import java.io.File

object ProjectConfigLoader {
  val defaultConfigFile = "scaleda.yml"

  def loadConfig(path: String): ProjectConfig = YamlBuilder(new File(path), classOf[ProjectConfig])
}

object ProjectConfigLoaderTester extends App {
  println(ProjectConfigLoader.loadConfig(getClass.getResource("/config/" + ProjectConfigLoader.defaultConfigFile).getFile))
}
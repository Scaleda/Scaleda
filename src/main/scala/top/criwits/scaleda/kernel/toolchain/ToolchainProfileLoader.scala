package top.criwits.scaleda
package kernel.toolchain

import io.circe._
import io.circe.yaml.parser

import scala.io.Source

object ToolchainProfileLoader {
  val defaultProfilesFile = "~/.scaleda/toolchains.yml"

  def loadProfiles(path: String): Unit = {
    val f = Source.fromFile(path)
    val content = f.getLines().toList.mkString("\n")
    f.close()


  }
}

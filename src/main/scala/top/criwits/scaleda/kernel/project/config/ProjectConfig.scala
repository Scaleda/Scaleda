package top.criwits.scaleda
package kernel.project.config

import io.circe.Decoder
import io.circe.yaml.parser

import scala.io.Source

// import org.virtuslab.yaml._

case class ProjectConfig
(
  name: String = "default-project",
  `type`: String = "rtl",
  src: String = ""
)

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"
  def loadConfig(path: String): ProjectConfig = {
    val f = Source.fromFile(path)
    val content = f.getLines().toList.mkString("\n")
    // println(s"content: ${content}")
    f.close()
    val decoded = parser.parse(content)
    val v = decoded.map(e => {
      // println(e)
      implicit val decoder: Decoder[ProjectConfig] = Decoder.instance(c =>
        for {
          n <- c.downField("name").as[String]
          t <- c.downField("type").as[String]
          s <- c.downField("src").as[String]
        } yield ProjectConfig(n, t, s))
      e.as[ProjectConfig]
    })
    v.flatMap(x => x).getOrElse(ProjectConfig())
  }
}
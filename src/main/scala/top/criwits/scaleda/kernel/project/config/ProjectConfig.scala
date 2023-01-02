package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.module.scala.JacksonModule

case class ProjectConfig
(
  name: String = "default-project",
  `type`: String = "rtl",
  src: String = ""
)

// object ProjectConfig extends ProjectConfig
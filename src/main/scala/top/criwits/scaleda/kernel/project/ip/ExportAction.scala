package top.criwits.scaleda
package kernel.project.ip

import kernel.project.config.ProjectConfig

case class ExportAction(acceptToolchains: Seq[String], replace: ProjectConfig)

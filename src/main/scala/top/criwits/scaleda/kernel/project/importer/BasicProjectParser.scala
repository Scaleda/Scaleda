package top.criwits.scaleda
package kernel.project.importer

import kernel.project.config.ProjectConfig

import java.io.File

trait BasicProjectParser {
  def parseProject(path: File): ProjectConfig
}

package top.scaleda
package kernel.project.importer

import kernel.project.config.ProjectConfig
import kernel.project.detect.BasicProjectDetector

import java.io.File

trait BasicTargetParser {

  /** Parse a directory as target dir, then returns [[TargetConfig]]<br/>
    * It is required to check [[BasicProjectDetector]].detect first<br/>
    * WARN: When parse errors, exceptions will be thrown.
    * @param path project path
    * @return parsed target config
    */
  def parseAsProject(path: File): ProjectConfig
}

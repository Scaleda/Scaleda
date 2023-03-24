package top.criwits.scaleda
package kernel.project.importer

import kernel.project.config.TargetConfig
import kernel.project.detect.BasicProjectDetector

import java.io.File

trait BasicTargetParser {

  /** Parse a directory as target dir, then returns [[TargetConfig]]<br/>
    * it is required to check [[BasicProjectDetector]].detect first
    * @param path project path
    * @return parsed target config
    */
  def parseAsTarget(path: File): TargetConfig
}

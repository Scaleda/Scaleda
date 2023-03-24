package top.criwits.scaleda
package kernel.project.detect

import java.io.File

trait BasicProjectDetector {
  def detect(path: File): Boolean = false
}

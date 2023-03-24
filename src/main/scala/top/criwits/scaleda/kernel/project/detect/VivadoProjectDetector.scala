package top.criwits.scaleda
package kernel.project.detect
import java.io.{File, FilenameFilter}

// TODO: move this to Vivado.scala
class VivadoProjectDetector extends BasicProjectDetector {
  override def detect(path: File) = {
    // only detect <project-name>.xpr file now
    if (!path.exists() || path.isDirectory) false
    else {
      val projectFile = path.listFiles(new FilenameFilter {
        override def accept(file: File, s: String): Boolean = s.endsWith(".xpr")
      }).headOption
      if (projectFile.isEmpty) false
      else true
    }
  }
}

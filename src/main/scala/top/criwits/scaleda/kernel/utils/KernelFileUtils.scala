package top.criwits.scaleda
package kernel.utils

import top.criwits.scaleda.kernel.project.config.ProjectConfig
import top.criwits.scaleda.kernel.project.config.ProjectConfig.config

import java.io.{File, FilenameFilter}
import java.util.regex.Pattern
import scala.io.Source

object KernelFileUtils {
  def getAllSourceFiles(sourceDir: File = new File(new File(ProjectConfig.projectBase.get).getAbsolutePath,
    ProjectConfig.config.source), suffixing: Set[String] = Set("v")): Seq[File] =
    sourceDir.listFiles(new FilenameFilter {
      override def accept(file: File, s: String) =
        suffixing.map(suffix => s.endsWith(s".${suffix}"))
          .reduceOption((a, b) => a || b).getOrElse(false)
    }).toList

  def getAbsolutePath(path: String, projectBase: Option[String] = ProjectConfig.projectBase): Option[String] = {
    val file = new File(path)
    file.isAbsolute match {
      case true =>
        projectBase match {
          case Some(base) =>
            Some(new File(new File(base), path).getAbsolutePath)
          case None => None
        }
      case false => Some(file.getAbsolutePath)
    }
  }

  def getModuleTitle(verilogFile: File): Option[String] = {
    val content = Source.fromFile(verilogFile).mkString
    val p = Pattern.compile("((.|\\n|\\r)*?)(module)(\\s)(\\w+)(\\s*)(((#)(\\s*)(\\((.|\\n|\\r)*?\\))(\\s*))?)(\\((.|\\n|\\r)*?\\))(\\s*)(;)((.|\\n|\\r)*?)(endmodule)((.|\\n|\\r)*?)")
    val m = p.matcher(content)
    if (m.find()) {
      Some(m.group(5))
    } else {
      None
    }
  }

  def getModuleFile(module: String): Option[File] = {
    getAllSourceFiles().foreach(f => getModuleTitle(f) match {
      case Some(s) => return Some(f)
      case None =>
    })
    None
  }
}


package top.criwits.scaleda
package kernel.utils

import java.io.{File, FilenameFilter}

object KernelFileUtils {
  def getAllSourceFiles(sourceDir: File, suffixing: Set[String] = Set("v")): Seq[File] =
    sourceDir.listFiles(new FilenameFilter {
      override def accept(file: File, s: String) =
        suffixing.map(suffix => s.endsWith(s".${suffix}"))
          .reduceOption((a, b) => a || b).getOrElse(false)
    }).toList
}


package top.criwits.scaleda
package kernel.utils

import java.io.File

object Paths {
  def createDirIfNonExists(f: File): File = {
    if (!f.exists()) f.mkdirs()
    f
  }

  def permanentConfigDir: File = createDirIfNonExists(new File(OS.getUserHome, ".scaleda"))

  def permanentToolChainsDir: File = createDirIfNonExists(new File(permanentConfigDir, "toolchains"))

  def pwd = new File(System.getProperty("user.dir"))

  def findExecutableOnPath(name: String): Option[String] = {
    for (dirname <- sys.env.getOrElse("PATH", "").split(File.pathSeparator)) {
      val file = new File(dirname, name)
      if (file.isFile && file.canExecute) return Some(file.getAbsolutePath)
    }
    None
  }
}

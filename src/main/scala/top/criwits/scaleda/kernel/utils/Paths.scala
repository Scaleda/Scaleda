package top.criwits.scaleda
package kernel.utils

import java.io.File

object Paths {
  val SCALEDA_HOME: String = "SCALEDA_HOME"

  def createDirIfNonExists(f: File): File = {
    if (!f.exists()) f.mkdirs()
    f
  }

  def getEnvHome: Option[String] = {
    val systemEnv = System.getenv()
    if (systemEnv.containsKey(SCALEDA_HOME)) {
      Some(systemEnv.get(SCALEDA_HOME))
    } else {
      None
    }
  }

  def getConfigDir: File = {
    getEnvHome match {
      case Some(path) => createDirIfNonExists(new File(path))
      case None => createDirIfNonExists(new File(OS.getUserHome, ".scaleda"))
    }
  }

  def getToolchainsDir: File = createDirIfNonExists(new File(getConfigDir, "toolchains"))

  def pwd = new File(System.getProperty("user.dir"))

  def findExecutableOnPath(name: String): Option[String] = {
    val possibleFile = new File(name)
    if (possibleFile.exists()) return Some(possibleFile.getAbsolutePath)
    for (dirname <- sys.env.getOrElse("PATH", "").split(File.pathSeparator)) {
      val file = new File(dirname, name)
      if (file.isFile && file.canExecute) return Some(file.getAbsolutePath)
    }
    None
  }
}

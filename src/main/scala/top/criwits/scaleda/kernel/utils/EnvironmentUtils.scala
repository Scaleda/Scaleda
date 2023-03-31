package top.criwits.scaleda
package kernel.utils

import java.io.File
import scala.collection.mutable
import scala.io.Source

object EnvironmentUtils {
  // In JVM, we cannot modify global environment variables, so we should copy one for modification
  object Backup {
    val env = new mutable.HashMap[String, String]()
    env.addAll(sys.env)
    LoadManually.run()
  }

  private object LoadManually {
    def run(): Unit = {
      try {
        val s = Source.fromFile(new File(".env"))
        val lines = s.getLines()
        s.close()
        for (line <- lines) {
          if (line.contains("=")) {
            val split = line.split('=')
            if (split.length == 2) {
              Backup.env.put(split.head, split.last)
            }
          }
        }
      } catch {
        case e: Throwable =>
          KernelLogger.debug("cannot load extra .env file:", e)
      }
    }
  }
}

package top.criwits.scaleda
package kernel.utils

import java.io.File
import scala.collection.mutable
import scala.io.Source

object EnvironmentUtils {
  object Backup {
    val env = new mutable.HashMap[String, String]()
    env.addAll(sys.env)
    LoadManually.run()
  }

  private object LoadManually {
    def run(): Unit = {
      try {
        for (line <- Source.fromFile(new File(".env")).getLines()) {
          if (line.contains("=")) {
            val split = line.split('=')
            if (split.length == 2) {
              Backup.env.put(split.head, split.last)
            }
          }
        }
      } catch {
        case e: Throwable =>
          KernelLogger.warn("cannot load extra .env file:", e)
      }
    }
  }
}

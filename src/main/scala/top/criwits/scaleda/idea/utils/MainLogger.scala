package top.criwits.scaleda
package idea.utils

import kernel.utils.{AbstractLogger, LogLevel}

import com.intellij.openapi.diagnostic.Logger

class MainLogger

object MainLogger extends AbstractLogger {
  val logger: Logger = Logger.getInstance(classOf[MainLogger])

  //noinspection DuplicatedCode
  override def log(msg: String, level: LogLevel.Value): Unit = {
    import LogLevel._
    level match {
      case Debug => logger.debug(msg)
      case Info => logger.info(msg)
      case Warn => logger.warn(msg)
      case _ => logger.error(msg)
    }
  }
}

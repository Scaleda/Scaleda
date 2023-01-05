package top.criwits.scaleda
package idea.utils

import kernel.utils.{AbstractLogger, LogLevel}

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import com.intellij.openapi.diagnostic.Logger

class MainLogger

object MainLogger extends AbstractLogger {
  val logger: Logger = Logger.getInstance(classOf[MainLogger])
  var consoleView: Option[ConsoleView] = None

  //noinspection DuplicatedCode
  override def log(msg: String, level: LogLevel.Value): Unit = {
    import LogLevel._
    level match {
      case Debug => logger.debug(msg)
      case Info => logger.info(msg)
      case Warn => logger.warn(msg)
      case _ => logger.error(msg)
    }
    if (consoleView.nonEmpty) {
      consoleView.get.print(msg, level match {
        case Debug => ConsoleViewContentType.LOG_DEBUG_OUTPUT
        case Info => ConsoleViewContentType.LOG_INFO_OUTPUT
        case Warn => ConsoleViewContentType.LOG_WARNING_OUTPUT
        case _ => ConsoleViewContentType.LOG_ERROR_OUTPUT
      })
    }
  }
}

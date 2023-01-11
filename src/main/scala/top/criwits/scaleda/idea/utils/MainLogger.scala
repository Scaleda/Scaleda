package top.criwits.scaleda
package idea.utils

import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import com.intellij.openapi.diagnostic.Logger
import sourcecode.{File, Line, Name}

object MainLogger extends BasicLogger {
  private val name = "scaleda-idea"
  val logger: Logger = Logger.getInstance(name)
  var consoleView: Option[ConsoleView] = None

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit line: Line, file: File, name: Name) = {
    import LogLevel._
    val args = xs.mkString(" ")
    val msg = f"[${getSourceInfoText}] $args"
    level match {
      case Debug => logger.debug(msg)
      case Info => logger.info(msg)
      case Warn => logger.warn(msg)
      case _ => logger.error(msg)
    }
    if (consoleView.nonEmpty) {
      consoleView.get.print(s"$msg\n", level match {
        case Debug => ConsoleViewContentType.LOG_DEBUG_OUTPUT
        case Verbose => ConsoleViewContentType.LOG_VERBOSE_OUTPUT
        case Info => ConsoleViewContentType.LOG_INFO_OUTPUT
        case Warn => ConsoleViewContentType.LOG_WARNING_OUTPUT
        case _ => ConsoleViewContentType.LOG_ERROR_OUTPUT
      })
    }
    level match {
      case Warn | Error => Notification.logging(level, xs: _*)
      case _ => {}
    }
  }
}

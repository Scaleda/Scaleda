package top.criwits.scaleda
package idea.utils

import idea.utils.OutputLogger.logger
import kernel.utils.LogLevel.{Debug, Info, Verbose, Warn}
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import sourcecode.{File, Line, Name}

class ConsoleLogger(consoleView: ConsoleView) extends BasicLogger {
  override def logging[T](level: LogLevel.Value, xs: T*)(implicit line: Line, file: File, name: Name) = {
    val args = xs.mkString(" ")
    val msg = args
    level match {
      case Debug => logger.debug(msg)
      case Info => logger.info(msg)
      case Warn => logger.warn(msg)
      case _ => logger.error(msg)
    }
    consoleView.print(s"$msg\n", level match {
      case Debug => ConsoleViewContentType.LOG_DEBUG_OUTPUT
      case Verbose => ConsoleViewContentType.LOG_VERBOSE_OUTPUT
      case Info => ConsoleViewContentType.LOG_INFO_OUTPUT
      case Warn => ConsoleViewContentType.LOG_WARNING_OUTPUT
      case _ => ConsoleViewContentType.LOG_ERROR_OUTPUT
    })
  }
}

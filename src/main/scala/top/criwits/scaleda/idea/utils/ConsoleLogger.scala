package top.criwits.scaleda
package idea.utils

import kernel.utils.LogLevel.{Debug, Info, Verbose, Warn}
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import com.intellij.openapi.diagnostic.Logger
import sourcecode.{File, Line, Name}

class ConsoleLogger(consoleView: ConsoleView) extends BasicLogger {
  val logger: Logger = Logger.getInstance(getClass)

  setLevel(Debug)

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit line: Line, file: File, name: Name) = {
    val msg = xs.mkString(" ")
    level match {
      case Debug => logger.debug(msg)
      case Info => logger.info(msg)
      case Warn => logger.warn(msg)
      case _ => logger.error(msg)
    }
    consoleView.print(s"$msg\n", MainLogger.consoleLevel(level))
  }
}

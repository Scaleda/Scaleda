package top.criwits.scaleda
package idea.utils

import idea.windows.tool.logging.ScaledaLoggingService
import kernel.utils.LogLevel.{Debug, Info, Warn}
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.diagnostic.Logger
import sourcecode.{File, Line, Name}

/**
 * Create a logger binding to a console view.<br/>
 * Send logging messages to console directly and to logging service
 * @param consoleView console view
 * @param logSourceId log id for logging service
 */
class ConsoleLogger(
    consoleView: ConsoleView,
    logSourceId: Option[String] = None
) extends BasicLogger {
  val logger: Logger = Logger.getInstance(getClass)

  setLevel(Debug)

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ) = {
    val msg = xs.mkString(" ")
    level match {
      case Debug => logger.debug(msg)
      case Info  => logger.info(msg)
      case Warn  => logger.warn(msg)
      case _     => logger.error(msg)
    }
    consoleView.print(s"$msg\n", MainLogger.consoleLevel(level))
    logSourceId.foreach(source => {
      ProjectNow().foreach(
        _.getService(classOf[ScaledaLoggingService]).print(source, msg, level)
      )
    })
  }
}

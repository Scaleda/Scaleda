package top.scaleda
package idea.windows.bottomPanel.console

import idea.utils.{ProjectNow, ScaledaIdeaLogger}
import kernel.utils.LogLevel.{Debug, Info, Warn}
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}
import top.scaleda.idea.windows.bottomPanel.console.ConsoleLogger.consoleLevel

/**
 * Create a logger binding to a console view.<br/>
 * Send logging messages to console directly and to logging service
 * @param consoleView console view
 * @param logSourceId log id for logging service
 */
class ConsoleLogger(
    project: Project,
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
    consoleView.print(s"$msg\n", consoleLevel(level))
    logSourceId.foreach(source => {
      project.getService(classOf[ConsoleService]).print(source, msg, level)
    })
  }
}

object ConsoleLogger {
  import LogLevel._

  def consoleLevel(level: LogLevel.Value): ConsoleViewContentType =
    level match {
      case Debug   => ConsoleViewContentType.LOG_DEBUG_OUTPUT
      case Verbose => ConsoleViewContentType.LOG_VERBOSE_OUTPUT
      case Info    => ConsoleViewContentType.LOG_INFO_OUTPUT
      case Warn    => ConsoleViewContentType.LOG_WARNING_OUTPUT
      case _       => ConsoleViewContentType.LOG_ERROR_OUTPUT
    }
}
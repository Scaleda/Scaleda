package top.criwits.scaleda
package idea.utils

import idea.windows.tool.logging.ScaledaConsoleService
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.diagnostic.Logger
import sourcecode.{File, Line, Name}

object MainLogger extends BasicLogger {
  final val LOGGER_ID = "scaleda-idea"
  val logger: Logger = Logger.getInstance(LOGGER_ID)

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ) = {
    import LogLevel._
    val args = xs.mkString(" ")
    val msg = f"[${getSourceInfoText}] $args"
    level match {
      case Debug => logger.debug(msg)
      case Info  => logger.info(msg)
      case Warn  => logger.warn(msg)
      case _     => logger.error(msg)
    }
    ProjectNow().foreach(project => {
      val service = project.getService(classOf[ScaledaConsoleService])
      service.print(LOGGER_ID, s"$msg\n", MainLogger.consoleLevel(level))
      level match {
        case Warn | Error => Notification(project).logging(level, xs: _*)
        case _            => {}
      }
    })
  }

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

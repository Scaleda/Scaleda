package top.scaleda
package idea.utils

import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.diagnostic.Logger
import sourcecode.{File, Line, Name}

object ScaledaIdeaLogger extends BasicLogger {
  final val LOGGER_ID = "scaleda-kernel"
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
  }

}

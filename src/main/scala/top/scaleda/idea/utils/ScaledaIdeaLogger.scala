package top.scaleda
package idea.utils

import idea.windows.bottomPanel.ConsoleService
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Disposer
import sourcecode.{File, Line, Name}

object ScaledaIdeaLogger extends BasicLogger with Disposable {
  private final val LOGGER_ID = "scaleda"
  val logger: Logger          = Logger.getInstance(LOGGER_ID)

  def getId: String = LOGGER_ID

  private var service: Option[ConsoleService] = None

  def setConsoleService(consoleService: ConsoleService): Unit = {
    service = Some(consoleService)
    Disposer.register(consoleService, this)
  }

  override def dispose(): Unit = {
    service = None
  }

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ) = {
    import LogLevel._
    val args = xs.mkString(" ")
    val msg  = f"[${getSourceInfoText}] $args"
    service match {
      case Some(s) => s.print(msg, level, getId)
      case None    => println(msg)
    }
    level match {
      case Debug => logger.debug(msg)
      case Info  => logger.info(msg)
      case Warn  => logger.warn(msg)
      case _     => logger.error(msg)
    }
  }
}

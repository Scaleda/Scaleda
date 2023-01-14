package top.criwits.scaleda
package idea.utils

import idea.windows.tool.logging.ScaledaConsoleService
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}

class OutputLogger(project: Project) extends BasicLogger {
  val logger: Logger = Logger.getInstance(OutputLogger.LOGGER_ID)

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ) = {
    import LogLevel._
    val args = xs.mkString(" ")
    val msg = args
    level match {
      case Debug => logger.debug(msg)
      case Info  => logger.info(msg)
      case Warn  => logger.warn(msg)
      case _     => logger.error(msg)
    }

    val service = project.getService(classOf[ScaledaConsoleService])
    service.print(OutputLogger.LOGGER_ID, s"$msg\n", MainLogger.consoleLevel(level))
    level match {
      case Warn | Error => Notification(project).logging(level, xs: _*)
      case _            => {}
    }
  }
}

object OutputLogger {
  final val LOGGER_ID = "scaleda-output"
  def apply(project: Project): OutputLogger = new OutputLogger(project)
  def apply(): OutputLogger = new OutputLogger(ProjectNow.apply().get)
}

package top.criwits.scaleda
package idea.utils

import idea.windows.tool.ScaledaToolWindowFactory
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}

class OutputLogger(project: Project) extends BasicLogger {
  private val name = "scaleda-output"
  val logger: Logger = Logger.getInstance(name)

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

    // ScaledaToolWindowFactory
    //   .outputPanel(project)
    //   .consoleView
    //   .print(s"$msg\n", MainLogger.consoleLevel(level))
    level match {
      case Warn | Error => Notification(project).logging(level, xs: _*)
      case _            => {}
    }
  }
}

object OutputLogger {
  def apply(project: Project): OutputLogger = new OutputLogger(project)
}
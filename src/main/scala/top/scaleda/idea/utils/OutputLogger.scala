package top.scaleda
package idea.utils

import kernel.shell.ScaledaRunHandler
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}
import top.scaleda.idea.windows.bottomPanel.ConsoleService

/**
 * Logger for all external running stuff.
 * Message logged here will be sent to (1) console and (2) message list.
 * @param project current project
 */
class OutputLogger(project: Project) extends BasicLogger {
  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ): Unit = {
    val args = xs.mkString(" ")
    val msg  = args
    val service = project.getService(classOf[ConsoleService])
    service.print(s"$msg\n", level)
  }
}

object OutputLogger {
  def apply(project: Project): OutputLogger = new OutputLogger(project)
  // def apply(): OutputLogger                 = new OutputLogger(ProjectNow.apply().get)

  class Handler(project: Project) extends ScaledaRunHandler {
    override def onStdout(data: String): Unit = OutputLogger(project).info(data)

    override def onStderr(data: String): Unit = OutputLogger(project).warn(data)

    override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit =
      OutputLogger(project).info(
        s"command done, returns $returnValue, finishedAll: $finishedAll, meerError: $meetErrors"
      )
  }

  class StdErrToWarningHandler(project: Project) extends Handler(project) {
    override def onStderr(data: String): Unit = OutputLogger(project).warn(data)
  }

  class StdErrToInfoHandler(project: Project) extends Handler(project) {
    override def onStderr(data: String): Unit = OutputLogger(project).info(data)
  }
}

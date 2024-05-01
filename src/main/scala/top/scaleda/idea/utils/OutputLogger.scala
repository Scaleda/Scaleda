package top.scaleda
package idea.utils

import kernel.shell.ScaledaRunHandler
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}
import top.scaleda.idea.windows.bottomPanel.ConsoleService

/** Logger for all external running stuff.
  * Message logged here will be sent to (1) console and (2) message list.
  * @param project current project
  */
class OutputLogger(project: Project, id: String, displayName: String, switchTo: Boolean) extends BasicLogger {
  private val service = project.getService(classOf[ConsoleService])
  runInEdt {
    service.getConsoleTabManager.foreach(_.addConsoleTab(id, displayName, switchTo = switchTo))
  }

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ): Unit = {
    val args    = xs.mkString(" ")
    val msg     = args
    val success = service.print(s"$msg\n", level, id)
    if (!success) {
      // ScaledaIdeaLogger.debug(s"Failed to print to console: $key $msg")
    }
  }
}

object OutputLogger {
  def create(project: Project, id: String, displayName: String, switchTo: Boolean = true): OutputLogger =
    new OutputLogger(project, id, displayName, switchTo = switchTo)

  class Handler(project: Project, id: String, displayName: String, switchTo: Boolean = true) extends ScaledaRunHandler {
    private val logger = new OutputLogger(project, id, displayName, switchTo)

    override def onStdout(data: String): Unit = logger.info(data)

    override def onStderr(data: String): Unit = logger.warn(data)

    override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit =
      logger.info(
        s"command done, returns $returnValue, finishedAll: $finishedAll, meerError: $meetErrors"
      )
  }

  class StdErrToWarningHandler(project: Project, id: String, displayName: String, switchTo: Boolean = true)
      extends Handler(project, id, displayName) {
    private val logger = new OutputLogger(project, id, displayName, switchTo)

    override def onStderr(data: String): Unit = logger.warn(data)
  }

  class StdErrToInfoHandler(project: Project, id: String, displayName: String, switchTo: Boolean = true)
      extends Handler(project, id, displayName) {
    private val logger = new OutputLogger(project, id, displayName, switchTo)

    override def onStderr(data: String): Unit = logger.info(data)
  }
}

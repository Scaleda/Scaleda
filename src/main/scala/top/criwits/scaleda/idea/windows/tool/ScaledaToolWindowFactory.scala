package top.criwits.scaleda
package idea.windows.tool

import idea.utils.{MainLogger, OutputLogger}
import idea.windows.tool.logging.{ConsoleTabManager, ScaledaLoggingService}

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory, ToolWindowManager}
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.windows.tool.message.ScaledaMessageTab

class ScaledaToolWindowFactory extends ToolWindowFactory {
  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    val service = project.getService(classOf[ScaledaLoggingService])
    val tabManager =
      new ConsoleTabManager(project, toolWindow.getContentManager)
    Disposer.register(service, tabManager)

    val messageTab = ScaledaMessageTab(project)

    tabManager.addPanel(messageTab, ScaledaBundle.message("windows.tool.log.message.title"))
    tabManager.addTab(OutputLogger.LOGGER_ID, ScaledaBundle.message("windows.tool.log.output.title"), switchTo = false)
    tabManager.addTab(MainLogger.LOGGER_ID, ScaledaBundle.message("windows.tool.log.main.title"), switchTo = false)
  }
}

object ScaledaToolWindowFactory {
  val WINDOW_ID = "Scaleda Messages"
}

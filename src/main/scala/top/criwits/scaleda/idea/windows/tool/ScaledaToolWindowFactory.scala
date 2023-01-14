package top.criwits.scaleda
package idea.windows.tool

import idea.utils.{MainLogger, OutputLogger}
import idea.windows.tool.logging.{ConsoleTabManager, ScaledaLoggingService}

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory, ToolWindowManager}
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

    val messageTab = new ScaledaMessageTab(project)
    tabManager.addPanel(messageTab.getPanel, "Messages")

    tabManager.addTab(OutputLogger.LOGGER_ID, "Output Logger", switchTo = false)
    tabManager.addTab(MainLogger.LOGGER_ID, "Main Logger", switchTo = false)
  }
}

object ScaledaToolWindowFactory {
  def toolWindow(project: Project): ToolWindow =
    ToolWindowManager.getInstance(project).getToolWindow("Scaleda")
}

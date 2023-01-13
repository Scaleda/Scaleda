package top.criwits.scaleda
package idea.windows.tool

import idea.utils.{MainLogger, OutputLogger}
import idea.windows.tool.logging.{ConsoleTabManager, ScaledaConsoleService}

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{
  ToolWindow,
  ToolWindowFactory,
  ToolWindowManager
}

class ScaledaToolWindowFactory extends ToolWindowFactory {
  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    val service = project.getService(classOf[ScaledaConsoleService])
    val tabManager =
      new ConsoleTabManager(project, toolWindow.getContentManager)
    Disposer.register(service, tabManager)

    tabManager.addTab(OutputLogger.LOGGER_ID, "Output Logger")
    tabManager.addTab(MainLogger.LOGGER_ID, "Main Logger")
  }
}

object ScaledaToolWindowFactory {
  def toolWindow(project: Project): ToolWindow =
    ToolWindowManager.getInstance(project).getToolWindow("Scaleda")
}

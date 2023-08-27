package top.scaleda
package idea.windows.rightPanel

import idea.project.io.YmlRootManager

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import top.scaleda.idea.ScaledaBundle

class ScaledaTasksWindowFactory extends ToolWindowFactory {
  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    val contentManager = toolWindow.getContentManager
    val panel = new ScaledaTasksPanel(project)
    contentManager.addContent(
      contentManager.getFactory.createContent(panel, ScaledaBundle.message("tasks.tool.window.title"), false))
  }
}

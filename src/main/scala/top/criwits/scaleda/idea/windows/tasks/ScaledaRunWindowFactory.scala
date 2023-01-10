package top.criwits.scaleda
package idea.windows.tasks

import idea.ScaledaBundle

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}

class ScaledaRunWindowFactory extends ToolWindowFactory {
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    toolWindow.setTitle(ScaledaBundle.message("tasks.tool.window.title"))
    val contentManager = toolWindow.getContentManager
    val options = new ScaledaRunToolWindowOption(project)

  }
}

object ScaledaRunWindowFactory {
  val WINDOW_ID = "scaleda"
}
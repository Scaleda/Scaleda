package top.criwits.scaleda
package idea.windows.deprecated

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory, ToolWindowManager}
import com.intellij.ui.content.ContentFactory

class ScaledaTargetWindowFactory extends ToolWindowFactory {
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val treePanel = new ScaledaTargetWindowPanel(project)
    val contentFactory = ContentFactory.SERVICE.getInstance()

    val content = contentFactory.createContent(treePanel, "", false) // FIXME


    toolWindow.getContentManager.addContent(content)
  }
}


object ScaledaTargetWindowFactory {
  def toolWindow(project: Project): ToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Scaleda Targets")

  def targetPanel(project: Project): ScaledaTargetWindowPanel = toolWindow(project)
    .getContentManager
    .getContent(0).getComponent
    .asInstanceOf[ScaledaTargetWindowPanel]
}
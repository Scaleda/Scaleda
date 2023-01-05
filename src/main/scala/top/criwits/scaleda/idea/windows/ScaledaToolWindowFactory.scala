package top.criwits.scaleda
package idea.windows

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory, ToolWindowManager}
import com.intellij.ui.content.ContentFactory
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.utils.Icons

import javax.swing.Icon

class ScaledaToolWindowFactory extends ToolWindowFactory {
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val contentFactory = ContentFactory.SERVICE.getInstance()
    val outputPanel = new ScaledaToolWindowOutputPanel(project)
    val outputTab = contentFactory.createContent(outputPanel, ScaledaBundle.message("windows.tool.output.title"), false)
    toolWindow.getContentManager.addContent(outputTab)

    val logPanel = new ScaledaToolWindowOutputPanel(project)
    val logTab = contentFactory.createContent(logPanel, "Log", false)
    toolWindow.getContentManager.addContent(logTab)
  }

}

object ScaledaToolWindowFactory {
  def toolWindow(project: Project): ToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Scaleda")
  def outputPanel(project: Project): ScaledaToolWindowOutputPanel = toolWindow(project)
    .getContentManager
    .getContent(0).getComponent
    .asInstanceOf[ScaledaToolWindowOutputPanel]
  def logPanel(project: Project): ScaledaToolWindowOutputPanel = toolWindow(project)
    .getContentManager
    .getContent(1).getComponent
    .asInstanceOf[ScaledaToolWindowOutputPanel]
}

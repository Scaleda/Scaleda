package top.criwits.scaleda
package idea.windows

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory, ToolWindowManager}
import com.intellij.ui.content.ContentFactory
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.utils.{Icons, MainLogger}

import javax.swing.Icon

class ScaledaToolWindowFactory extends ToolWindowFactory {
  import ScaledaToolWindowFactory._
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    outputPanel = Some(new ScaledaToolWindowOutputPanel(project))
    logPanel = Some(new ScaledaToolWindowOutputPanel(project))
    val contentFactory = ContentFactory.SERVICE.getInstance()
    val outputTab = contentFactory.createContent(outputPanel.get, ScaledaBundle.message("windows.tool.output.title"), false)
    val logTab = contentFactory.createContent(logPanel.get, "Log", false)

    toolWindow.getContentManager.addContent(outputTab)
    toolWindow.getContentManager.addContent(logTab)
  }

}

object ScaledaToolWindowFactory {
  private var outputPanel: Option[ScaledaToolWindowOutputPanel] = None
  private var logPanel: Option[ScaledaToolWindowOutputPanel] = None
  def toolWindow(project: Project): ToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Scaleda")
  // def outputPanel(project: Project): ScaledaToolWindowOutputPanel = toolWindow(project)
  //   .getContentManager
  //   .getContent(0)
  //   .asInstanceOf[ScaledaToolWindowOutputPanel]
  // TODO: load this classes from get content...
  def outputPanel(project: Project): ScaledaToolWindowOutputPanel = outputPanel.get
  def logPanel(project: Project): ScaledaToolWindowOutputPanel = logPanel.get
}

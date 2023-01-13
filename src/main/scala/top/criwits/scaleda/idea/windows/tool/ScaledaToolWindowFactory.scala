package top.criwits.scaleda
package idea.windows.tool

import idea.windows.tool.logging.{ConsoleTabManager, ScaledaLoggingService}

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
    // val contentFactory = ContentFactory.SERVICE.getInstance()
    // val logPanel = new ScaledaToolWindowOutputPanel(project)
    // val logTab = contentFactory.createContent(logPanel, "Log", false)
    // toolWindow.getContentManager.addContent(logTab)
    //
    // val outputPanel = new ScaledaToolWindowOutputPanel(project)
    // val outputTab = contentFactory.createContent(outputPanel, ScaledaBundle.message("windows.tool.output.title"), false)
    // toolWindow.getContentManager.addContent(outputTab)
    val service = project.getService(classOf[ScaledaLoggingService])
    val tabManager =
      new ConsoleTabManager(project, toolWindow.getContentManager)
    Disposer.register(service, tabManager)
    // val toolWindowEx = toolWindow.asInstanceOf[ToolWindowEx]
    // toolWindowEx.setTabActions()

    tabManager.addTab("MainLogger", "Main Logger")
    tabManager.addTab("OutputLogger", "Output Logger")
  }
}

object ScaledaToolWindowFactory {
  def toolWindow(project: Project): ToolWindow =
    ToolWindowManager.getInstance(project).getToolWindow("Scaleda")
  // def outputPanel(project: Project): ScaledaToolWindowOutputPanel = toolWindow(project)
  //   .getContentManager
  //   .getContent(1).getComponent
  //   .asInstanceOf[ScaledaToolWindowOutputPanel]
  // def logPanel(project: Project): ScaledaToolWindowOutputPanel = toolWindow(project)
  //   .getContentManager
  //   .getContent(0).getComponent
  //   .asInstanceOf[ScaledaToolWindowOutputPanel]
}

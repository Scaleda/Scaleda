package top.criwits.scaleda
package idea.windows.tool

import idea.ScaledaBundle
import idea.utils.{MainLogger, OutputLogger, ProjectNow, invokeLater}
import idea.windows.tool.logging.{ConsoleTabManager, ScaledaLoggingService}
import idea.windows.tool.message.ScaledaMessageTab

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}

class ScaledaToolWindowFactory extends ToolWindowFactory {
  private var created = false
  override def init(toolWindow: ToolWindow) = {
    super.init(toolWindow)
    MainLogger.info("ScaledaToolWindowFactory#init")
    invokeLater {
      // ProjectNow().foreach(p => createToolWindowContent(p, toolWindow))
    }
  }

  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    if (created) return
    else created = true
    MainLogger.info("ScaledaToolWindowFactory#createToolWindowContent")
    val service = project.getService(classOf[ScaledaLoggingService])
    val tabManager =
      new ConsoleTabManager(project, toolWindow.getContentManager)
    Disposer.register(service, tabManager)

    val messageTab = ScaledaMessageTab(project, tabManager = Some(tabManager))

    tabManager.addPanel(messageTab, messageTab.getDisplayName)
    tabManager.addTab(OutputLogger.LOGGER_ID, ScaledaBundle.message("windows.tool.log.output.title"), switchTo = false)
    tabManager.addTab(MainLogger.LOGGER_ID, ScaledaBundle.message("windows.tool.log.main.title"), switchTo = false)
  }
}

object ScaledaToolWindowFactory {
  val WINDOW_ID = "Scaleda Messages"
}

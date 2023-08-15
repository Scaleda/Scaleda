package top.scaleda
package idea.windows.bottomPanel

import idea.ScaledaBundle
import idea.utils.{OutputLogger, ProjectNow, ScaledaIdeaLogger, invokeLater}
import idea.windows.bottomPanel.console.{ConsoleTabManager, ConsoleService}
import idea.windows.bottomPanel.message.ScaledaMessageTab

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import top.scaleda.idea.project.io.YmlRootManager

class ScaledaMessagesWindowFactory extends ToolWindowFactory {
  override def shouldBeAvailable(project: Project): Boolean = {
    YmlRootManager.getInstance(project).getRoots.nonEmpty
  }

  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    val service = project.getService(classOf[ConsoleService])
    val tabManager =
      new ConsoleTabManager(project, toolWindow.getContentManager)
    Disposer.register(service, tabManager)

    val messageTab = ScaledaMessageTab(project, tabManager = Some(tabManager))

    tabManager.addPanel(messageTab, messageTab.getDisplayName)
    tabManager.addTab(OutputLogger.LOGGER_ID, ScaledaBundle.message("windows.tool.log.output.title"), switchTo = false)
  }
}

object ScaledaMessagesWindowFactory {
  val WINDOW_ID = "Scaleda Messages"
}

package top.scaleda
package idea.windows.bottomPanel

import idea.ScaledaBundle
import idea.project.io.YmlRootManager
import idea.windows.bottomPanel.console.ConsoleTabManager
import idea.windows.bottomPanel.message.MessageListPanel

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}

final class ScaledaMessagesWindowFactory extends ToolWindowFactory {
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

    val messageTab = new MessageListPanel(project)

    tabManager.addPanel(messageTab, ScaledaBundle.message("windows.tool.log.message.title"))
    tabManager.addConsoleTab(ScaledaBundle.message("windows.tool.log.console.title"), switchTo = false)
  }
}
package top.scaleda
package idea.windows.bottomPanel.console

import idea.windows.bottomPanel.ConsoleService

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

class ConsoleTab(parent: ConsoleTabManager, project: Project, id: String, displayName: String) extends Disposable {
  private val panel = new ConsolePanel(this, project, id, displayName)
  panel.registerConsoleReceiver(false)

  Disposer.register(parent, this)

  def getParent: ConsoleTabManager = parent

  def getDisplayName: String = displayName
  def getMessageId: String   = id

  def getPanel: ConsolePanel = panel

  override def dispose(): Unit = {
    val service = project.getService(classOf[ConsoleService])
    service.removeListener(panel.receiver)
  }
}

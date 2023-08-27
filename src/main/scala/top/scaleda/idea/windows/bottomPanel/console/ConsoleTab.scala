package top.scaleda
package idea.windows.bottomPanel.console

import idea.windows.bottomPanel.ConsoleService

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

class ConsoleTab(project: Project) extends Disposable {
  private val panel = new ConsolePanel(this, project)

  def getPanel: ConsolePanel = panel

  override def dispose(): Unit = {
    val service = project.getService(classOf[ConsoleService])
    service.removeListener(panel.receiver)
  }
}

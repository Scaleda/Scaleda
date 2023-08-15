package top.scaleda
package idea.windows.bottomPanel.console

import idea.windows.bottomPanel.ScaledaConsolePanel

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

class ConsoleTab(project: Project, logSourceId: String) extends Disposable {
  private val panel = new ScaledaConsolePanel(this, project, logSourceId)

  def getPanel = panel

  override def dispose(): Unit = {
    val service = project.getService(classOf[ConsoleService])
    service.removeListener(logSourceId)
  }
}

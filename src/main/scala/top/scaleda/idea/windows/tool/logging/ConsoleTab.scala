package top.scaleda
package idea.windows.tool.logging

import idea.windows.tool.ScaledaToolWindowOutputPanel

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

class ConsoleTab(project: Project, logSourceId: String) extends Disposable {
  private val panel = new ScaledaToolWindowOutputPanel(this, project, logSourceId)

  def getPanel = panel

  override def dispose() = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.removeListener(logSourceId)
  }
}

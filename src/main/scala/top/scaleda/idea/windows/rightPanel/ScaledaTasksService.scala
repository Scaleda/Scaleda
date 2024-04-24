package top.scaleda
package idea.windows.rightPanel

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

class ScaledaTasksService(val project: Project) extends Disposable {
  private var panel: Option[ScaledaTasksPanel] = None

  def setPanel(panel: ScaledaTasksPanel): Unit = {
    this.panel = Some(panel)
  }

  def getPanel: Option[ScaledaTasksPanel] = panel

  override def dispose(): Unit = {
    panel = None
  }
}

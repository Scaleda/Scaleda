package top.criwits.scaleda
package idea.windows.tool.message

import idea.windows.tool.logging.ScaledaLoggingService

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

import javax.swing.{JLabel, JPanel}

class ScaledaMessageTab(project: Project) extends Disposable {
  private val msgSourceId = ScaledaMessageTab.MESSAGE_ID
  val mainPanel = new JPanel()
  mainPanel.add(new JLabel("LABEL"))

  def getPanel = mainPanel

  override def dispose() = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.stop(msgSourceId)
  }
}

object ScaledaMessageTab {
  private val MESSAGE_ID = "scaleda-message"
}

package top.criwits.scaleda
package idea.windows.tool.message

import idea.runner.task.ScaledaRunLastTaskAction
import idea.windows.tool.logging.{ScaledaLogReceiver, ScaledaLoggingService}
import kernel.utils.LogLevel

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionManager, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBList

import javax.swing.DefaultListModel

class ScaledaMessageTab(project: Project)
    extends SimpleToolWindowPanel(false, true)
    with Disposable {
  private val msgSourceId = ScaledaMessageTab.MESSAGE_ID
  private val dataModel = new DefaultListModel[ScaledaMessage]()
  private val logReceiver = new ScaledaLogReceiver {
    override def print(text: String, level: LogLevel.Value) =
      dataModel.addElement(ScaledaMessage(text = text, level = level))
  }
  private val service = project.getService(classOf[ScaledaLoggingService])
  service.addListener(msgSourceId, logReceiver)
  private val listComponent = new JBList[ScaledaMessage](dataModel)
  private val renderer = new ScaledaMessageRenderer
  listComponent.setCellRenderer(renderer)

  val group = new DefaultActionGroup()
  group.add(new ScaledaRunLastTaskAction)
  group.addSeparator()
  group.add(new ScaledaRunLastTaskAction)

  val toolbar = ActionManager
    .getInstance()
    .createActionToolbar("Scaleda Message Toolbar", group, false)
  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)
  setContent(listComponent)

  override def dispose() = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.stop(msgSourceId)
  }
}

object ScaledaMessageTab {
  private val MESSAGE_ID = "scaleda-message"
}

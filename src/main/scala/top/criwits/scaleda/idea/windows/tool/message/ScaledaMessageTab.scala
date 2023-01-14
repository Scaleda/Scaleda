package top.criwits.scaleda
package idea.windows.tool.message

import idea.runner.task.ScaledaRunLastTaskAction
import idea.windows.tool.logging.{ScaledaLogReceiver, ScaledaLoggingService}
import kernel.utils.LogLevel

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionManager, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBList
import top.criwits.scaleda.kernel.toolchain.Toolchain

import javax.swing.DefaultListModel

class ScaledaMessageTab(project: Project)
    extends SimpleToolWindowPanel(false, true)
    with Disposable {
  private val msgSourceId = ScaledaMessageTab.MESSAGE_ID
  private val dataModel = new DefaultListModel[ScaledaMessage]()
  private val messageParser = new ScaledaMessageParser(dataModel)
  private val service = project.getService(classOf[ScaledaLoggingService])
  // add all known toolchain types
  Toolchain.toolchains.keys.foreach(toolchain =>
    service.addListener(s"$msgSourceId-$toolchain", messageParser))
  private val listComponent = new JBList[ScaledaMessage](dataModel)
  private val renderer = new ScaledaMessageRenderer
  listComponent.setCellRenderer(renderer)

  private val clearMessageAction = new AnAction() {
    override def actionPerformed(e: AnActionEvent) = dataModel.clear()
  }

  val group = new DefaultActionGroup()
  group.add(new ScaledaRunLastTaskAction)
  group.addSeparator()
  group.add(clearMessageAction)

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
  val MESSAGE_ID = "scaleda-message"
}

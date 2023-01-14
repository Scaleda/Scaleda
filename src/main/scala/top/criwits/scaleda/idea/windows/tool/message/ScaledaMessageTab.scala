package top.criwits.scaleda
package idea.windows.tool.message

import idea.ScaledaBundle
import idea.runner.task.ScaledaRunLastTaskAction
import idea.windows.tool.logging.ScaledaLoggingService
import kernel.toolchain.Toolchain

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{
  ActionManager,
  AnAction,
  AnActionEvent,
  DefaultActionGroup
}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.{JBList, JBScrollPane}

import javax.swing.{BoxLayout, DefaultListModel, JPanel}

class ScaledaMessageTab(project: Project)
    extends SimpleToolWindowPanel(false, true)
    with Disposable {
  private val msgSourceId = ScaledaMessageTab.MESSAGE_ID
  private val dataModel = new DefaultListModel[ScaledaMessage]()
  private val messageParser = new ScaledaMessageParser(dataModel)
  private val service = project.getService(classOf[ScaledaLoggingService])
  // add all known toolchain types
  Toolchain.toolchains.keys.foreach(toolchain =>
    service.addListener(s"$msgSourceId-$toolchain", messageParser)
  )
  private val listComponent = new JBList[ScaledaMessage](dataModel)
  private val scrollbar = new JBScrollPane()
  // listComponent.setAutoscrolls(true)
  listComponent.setAutoscrolls(false)
  private val renderer = new ScaledaMessageRenderer
  listComponent.setCellRenderer(renderer)

  private val clearMessageAction = new AnAction(
    ScaledaBundle.message("windows.message.action.clear"),
    ScaledaBundle.message("windows.message.action.clear"),
    AllIcons.Diff.Remove
  ) {
    override def actionPerformed(e: AnActionEvent) = dataModel.clear()
  }

  // TODO: fix this
  private val toggleScrollAction = new AnAction(
    ScaledaBundle.message("windows.message.action.scroll"),
    ScaledaBundle.message("windows.message.action.scroll"),
    AllIcons.General.AutoscrollFromSource
  ) {
    override def actionPerformed(e: AnActionEvent) = listComponent.setAutoscrolls(!listComponent.getAutoscrolls)
  }

  val group = new DefaultActionGroup()
  group.add(clearMessageAction)
  group.add(toggleScrollAction)

  val toolbar = ActionManager
    .getInstance()
    .createActionToolbar("Scaleda Message Toolbar", group, false)
  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)
  val panel = new JPanel()
  panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS))
  panel.add(listComponent)
  scrollbar.setViewportView(listComponent)
  panel.add(scrollbar)
  setContent(panel)

  override def dispose() = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.stop(msgSourceId)
  }
}

object ScaledaMessageTab {
  val MESSAGE_ID = "scaleda-message"
}

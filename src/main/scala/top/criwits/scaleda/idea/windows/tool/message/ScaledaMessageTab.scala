package top.criwits.scaleda
package idea.windows.tool.message

import idea.ScaledaBundle
import idea.utils.MainLogger
import idea.windows.tool.logging.ScaledaLoggingService
import kernel.toolchain.Toolchain

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionManager, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.{JBList, JBScrollPane}

import javax.swing.event.ListSelectionEvent
import javax.swing.{BoxLayout, DefaultListModel, JPanel}
import scala.collection.mutable.ArrayBuffer
import scala.jdk.javaapi.CollectionConverters

class ScaledaMessageTab(project: Project)
    extends SimpleToolWindowPanel(false, true)
    with Disposable {
  private val msgSourceId = ScaledaMessageTab.MESSAGE_ID
  // private val data = ArrayBuffer[ScaledaMessage]()
  private var sortByLevel = false
  private val dataModel = new DefaultListModel[ScaledaMessage]()

  def flushData(): Unit = {
    // if (sortByLevel) {
    //   data.sortInPlaceWith((a, b) => {
    //     if (a.level < b.level) true
    //     else if (a.level == b.level) a.time < b.time
    //     else false
    //   })
    // }
    // // NOTE that if we clear all data in dataModel and refill data, select operation will failed!!
    // dataModel.clear()
    // dataModel.addAll(CollectionConverters.asJava(data))
  }

  private val messageParser = new ScaledaMessageParser(message => {
    // data.addOne(message)
    dataModel.addElement(message)
    flushData()
  })
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
    override def actionPerformed(e: AnActionEvent) = {
      // data.clear()
      dataModel.clear()
    }
  }

  private val toggleSortAction = new AnAction(
    ScaledaBundle.message("windows.message.action.sort"),
    ScaledaBundle.message("windows.message.action.sort"),
    AllIcons.General.AutoscrollToSource
  ) {
    override def actionPerformed(e: AnActionEvent) = {
      sortByLevel = !sortByLevel
      flushData()
    }
  }
  // dataModel.addListDataListener(new ListDataListener() {
  //   override def intervalAdded(listDataEvent: ListDataEvent): Unit = ???
  //
  //   override def intervalRemoved(listDataEvent: ListDataEvent): Unit = ???
  //
  //   override def contentsChanged(listDataEvent: ListDataEvent): Unit = ???
  // })
  listComponent.addListSelectionListener(
    (listSelectionEvent: ListSelectionEvent) => {
      MainLogger.info(
        listSelectionEvent.toString,
        listSelectionEvent.getFirstIndex,
        listSelectionEvent.getLastIndex
      )
    }
  )

  val group = new DefaultActionGroup()
  group.add(clearMessageAction)
  group.add(toggleSortAction)

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

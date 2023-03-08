package top.criwits.scaleda
package idea.windows.tool.message

import idea.ScaledaBundle
import idea.runner.ScaledaRuntimeInfo
import idea.utils.MainLogger
import idea.windows.tool.logging.ScaledaLoggingService

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionManager, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.{ComboBox, SimpleToolWindowPanel}
import com.intellij.ui.components.{JBList, JBScrollPane}

import java.awt.BorderLayout
import java.awt.event.ItemEvent
import java.util.concurrent.LinkedBlockingQueue
import javax.swing.event.ListSelectionEvent
import javax.swing.{BoxLayout, DefaultListModel, JPanel}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.jdk.javaapi.CollectionConverters

class ScaledaMessageTab(project: Project) extends SimpleToolWindowPanel(false, true) with Disposable {
  private val msgSourceId   = ScaledaMessageTab.MESSAGE_ID
  private var sortByLevel   = false
  private val dataModel     = new DefaultListModel[ScaledaMessage]()
  private val listComponent = new JBList[ScaledaMessage](dataModel)

  private val data         = new mutable.HashMap[String, (ScaledaRuntimeInfo, ArrayBuffer[ScaledaMessage])]()
  private val viewComboBox = new ComboBox[String]()

  private def selectToViewById(selectedId: String) = {
    data
      .get(selectedId)
      .foreach(d => {
        val (rt, messages) = d
        // find renderer, if no, fallback to default
        val renderer =
          ScaledaMessageRenderer.getRendererMap.getOrElse(rt.profile.toolchainType, ScaledaMessageRendererImpl)
        listComponent.setCellRenderer(renderer)
        dataModel.synchronized {
          dataModel.clear()
          dataModel.addAll(CollectionConverters.asJava(messages))
        }
      })
  }

  viewComboBox.addItemListener(e => {
    if (e.getStateChange == ItemEvent.SELECTED) {
      val selectedId = e.getItem.asInstanceOf[String]
      selectToViewById(selectedId)
    }
  })

  private val messageQueue = new LinkedBlockingQueue[(ScaledaRuntimeInfo, ScaledaMessage)]()

  private val messageHandleThread = new Thread(() => {
    var running = true
    while (running) {
      try {
        val (rt, message) = messageQueue.take()
        if (viewComboBox.getItem == rt.id)
          dataModel.synchronized {
            dataModel.addElement(message)
          }
        data.synchronized {
          if (data.contains(rt.id)) data(rt.id)._2.addOne(message)
          else data.put(rt.id, (rt, ArrayBuffer(message)))
        }
        Thread.sleep(50)
      } catch {
        case e: InterruptedException => running = false
      }
    }
  })
  messageHandleThread.start()

  def attach(runtime: ScaledaRuntimeInfo): Unit = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.addListener(
      runtime.id,
      new ScaledaMessageParser(message => {
        messageQueue.put((runtime, message))
        MainLogger.info(s"[ RUNTIME: ${runtime.id} ] message insert:", message)
      })
    )
    dataModel.synchronized {
      dataModel.clear()
    }
    viewComboBox.addItem(runtime.id)
    viewComboBox.setSelectedItem(runtime.id)
  }

  def detachFromLogger(sourceId: String): Unit = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.removeListener(sourceId)
  }

  private val scrollbar = new JBScrollPane()
  // listComponent.setAutoscrolls(true)
  listComponent.setAutoscrolls(false)
  listComponent.setCellRenderer(ScaledaMessageRendererImpl)

  private val removeMessageAction = new AnAction(
    // TODO: i18n
    "Remove",
    "Remove",
    AllIcons.Diff.Remove
  ) {
    override def actionPerformed(e: AnActionEvent) = {
      dataModel.synchronized {
        dataModel.clear()
      }
      data.synchronized {
        data.clear()
      }
      viewComboBox.removeAllItems()
    }
  }

  private val clearMessageAction = new AnAction(
    ScaledaBundle.message("windows.message.action.clear"),
    ScaledaBundle.message("windows.message.action.clear"),
    AllIcons.Actions.DeleteTag
  ) {
    override def actionPerformed(e: AnActionEvent) = {
      dataModel.synchronized {
        dataModel.clear()
      }
      data.synchronized {
        data.remove(viewComboBox.getItem)
      }
    }
  }

  private val toggleSortAction = new AnAction(
    ScaledaBundle.message("windows.message.action.sort"),
    ScaledaBundle.message("windows.message.action.sort"),
    AllIcons.General.AutoscrollToSource
  ) {
    override def actionPerformed(e: AnActionEvent) = {
      sortByLevel = !sortByLevel
    }
  }
  listComponent.addListSelectionListener((listSelectionEvent: ListSelectionEvent) => {
    MainLogger.info(
      listSelectionEvent.toString,
      listSelectionEvent.getFirstIndex,
      listSelectionEvent.getLastIndex
    )
  })

  val group = new DefaultActionGroup()
  group.add(clearMessageAction)
  group.add(removeMessageAction)
  group.add(toggleSortAction)

  val toolbar = ActionManager
    .getInstance()
    .createActionToolbar("Scaleda Message Toolbar", group, false)
  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)
  private val panel = new JPanel()
  panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS))
  scrollbar.setViewportView(listComponent)
  panel.add(scrollbar)
  private val outerPanel = new JPanel()
  outerPanel.setLayout(new BorderLayout(0, 0))
  outerPanel.add(viewComboBox, BorderLayout.NORTH)
  outerPanel.add(panel, BorderLayout.CENTER)
  setContent(outerPanel)

  ScaledaMessageTab.INSTANCE = this

  override def dispose() = {
    ScaledaMessageTab.INSTANCE = null
    messageHandleThread.interrupt()
    val service = project.getService(classOf[ScaledaLoggingService])
    service.removeListener(msgSourceId)
  }
}

object ScaledaMessageTab {
  val MESSAGE_ID = "scaleda-message"

  private var INSTANCE: ScaledaMessageTab = _

  def instance = INSTANCE

  def apply(project: Project) = if (instance != null) instance else new ScaledaMessageTab(project)
}

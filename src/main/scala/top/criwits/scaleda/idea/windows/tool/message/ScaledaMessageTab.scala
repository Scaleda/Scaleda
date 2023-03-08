package top.criwits.scaleda
package idea.windows.tool.message

import idea.ScaledaBundle
import idea.runner.ScaledaRuntimeInfo
import idea.utils.MainLogger
import idea.windows.tool.logging.ScaledaLoggingService
import kernel.utils.LogLevel

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
import javax.swing.{BoxLayout, DefaultListModel, Icon, JPanel}
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

  private val allLevels = Seq(
    LogLevel.Debug,
    LogLevel.Verbose,
    LogLevel.Info,
    LogLevel.Warn,
    LogLevel.Error,
    LogLevel.Fatal
  )
  private val enabledLevel = new mutable.HashSet[LogLevel.Value]()
  // default to ignore debug and verbose
  enabledLevel.addAll(allLevels.slice(allLevels.indexOf(LogLevel.Info), allLevels.size))

  private def findRenderer(toolchainType: String) =
    ScaledaMessageRenderer.getRendererMap.getOrElse(toolchainType, ScaledaMessageRendererImpl)

  // Do sort here, filter is in other part
  private def sortData(): Unit = {
    if (sortByLevel) {
      dataModel.synchronized {
        val data = dataModel.toArray.map(_.asInstanceOf[ScaledaMessage])
        data.sortInPlaceWith((a, b) => a.level.id > b.level.id)
        dataModel.clear()
        dataModel.addAll(CollectionConverters.asJava(data))
      }
    }
  }

  private def selectToViewById(selectedId: String) = {
    if (selectedId != null) {
      data
        .get(selectedId)
        .foreach(d => {
          val (rt, messages) = d
          // find renderer, if no, fallback to default
          val renderer = findRenderer(rt.profile.toolchainType)
          listComponent.setCellRenderer(renderer)
          val filterData = messages.filter(m => enabledLevel.contains(m.level))
          dataModel.synchronized {
            dataModel.clear()
            dataModel.addAll(CollectionConverters.asJava(filterData))
          }
        })
      sortData()
    }
  }

  // viewComboBox can be auto select when data changes
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
        if (viewComboBox.getItem == rt.id && enabledLevel.contains(message.level)) {
          dataModel.synchronized {
            dataModel.addElement(message)
          }
          sortData()
        }
        data.synchronized {
          if (data.contains(rt.id)) data(rt.id)._2.addOne(message)
          else data.put(rt.id, (rt, ArrayBuffer(message)))
        }
        // A delay is necessary, or items in list will flash
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
    val renderer = findRenderer(runtime.profile.toolchainType)
    listComponent.setCellRenderer(renderer)
  }

  def detachFromLogger(sourceId: String): Unit = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.removeListener(sourceId)
  }

  private val scrollbar = new JBScrollPane()
  // listComponent.setAutoscrolls(true)
  listComponent.setAutoscrolls(false)
  listComponent.setCellRenderer(ScaledaMessageRendererImpl)

  // remove all message data
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

  // remove message data in this select
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
      viewComboBox.removeItem(viewComboBox.getItem)
    }
  }

  private def getToggleSortIcon: Icon =
    if (sortByLevel)
      AllIcons.RunConfigurations.Scroll_down
    else AllIcons.General.AutoscrollToSource
  private val toggleSortAction = new AnAction(
    ScaledaBundle.message("windows.message.action.sort"),
    ScaledaBundle.message("windows.message.action.sort"),
    getToggleSortIcon
  ) {
    override def actionPerformed(e: AnActionEvent) = {
      sortByLevel = !sortByLevel
      selectToViewById(viewComboBox.getItem)
    }

    override def update(e: AnActionEvent) = {
      // update icon
      e.getPresentation.setIcon(getToggleSortIcon)
    }
  }
  listComponent.addListSelectionListener((listSelectionEvent: ListSelectionEvent) => {
    MainLogger.info(
      listSelectionEvent.toString,
      listSelectionEvent.getFirstIndex,
      listSelectionEvent.getLastIndex
    )
  })

  private val levelIcons = Map(
    LogLevel.Debug   -> AllIcons.General.Note,
    LogLevel.Verbose -> AllIcons.Debugger.Db_muted_field_breakpoint,
    LogLevel.Info    -> AllIcons.General.Information,
    LogLevel.Warn    -> AllIcons.General.Warning,
    LogLevel.Error   -> AllIcons.General.Error,
    LogLevel.Fatal   -> AllIcons.Ide.FatalError
  )
  private val levelDisabledIcon = AllIcons.RunConfigurations.TestIgnored

  val levelActions = allLevels.map(level => {
    val icon = levelIcons(level)
    new AnAction(level.toString, level.toString, icon) {
      override def actionPerformed(e: AnActionEvent) = {
        if (enabledLevel.contains(level)) enabledLevel.remove(level)
        else enabledLevel.add(level)
        selectToViewById(viewComboBox.getItem)
      }

      override def update(e: AnActionEvent) = {
        if (enabledLevel.contains(level)) {
          e.getPresentation.setIcon(levelIcons(level))
          e.getPresentation.setText(s"Enabled $level")
        } else {
          e.getPresentation.setIcon(levelDisabledIcon)
          e.getPresentation.setText(s"Disabled $level")
        }
      }
    }
  })

  val group = new DefaultActionGroup()
  group.add(clearMessageAction)
  group.add(removeMessageAction)
  group.add(toggleSortAction)
  levelActions.foreach(group.add)

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

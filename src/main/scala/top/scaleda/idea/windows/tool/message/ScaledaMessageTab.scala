package top.scaleda
package idea.windows.tool.message

import idea.ScaledaBundle
import idea.project.IdeaManifestManager
import idea.utils.MainLogger
import idea.windows.tool.logging.{ConsoleTabManager, ScaledaLoggingService}
import kernel.utils.LogLevel

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.{ComboBox, SimpleToolWindowPanel}
import com.intellij.ui.components.{JBList, JBScrollPane}
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime

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

  private val data         = new mutable.HashMap[String, (ScaledaRuntime, ArrayBuffer[ScaledaMessage])]()
  private val viewComboBox = new ComboBox[String]()

  // TODO: jump to source when clicked
  // listComponent.addListSelectionListener(e => {
  //   data
  //     .get(viewComboBox.getItem)
  //     .foreach(d => {
  //       val array = d._2
  //       if (array.size >= e.getFirstIndex) {
  //         val item = array(e.getFirstIndex)
  //         if (item.file.nonEmpty) {
  //           RpcService.pushGotoInfo(RpcService.RpcGotoInfo(item.file.get, item.line.getOrElse(0), 0))
  //         }
  //       }
  //     })
  // })

  def getMessageData(key: String): Option[(ScaledaRuntime, ArrayBuffer[ScaledaMessage])] =
    data.get(key)

  def getFirstLargerLevelMessage(
      key: String,
      level: LogLevel.Value
  ): Option[ScaledaMessage] = getMessageData(key).flatMap(_._2.find(_.level.id >= level.id))

  def getFirstError(key: String)   = getFirstLargerLevelMessage(key, LogLevel.Error)
  def getFirstWarning(key: String) = getFirstLargerLevelMessage(key, LogLevel.Warn)
  def getFirstInfo(key: String)    = getFirstLargerLevelMessage(key, LogLevel.Info)

  def getCauseMessage(key: String): Option[ScaledaMessage] = {
    val v = getFirstError(key)
      .getOrElse(
        getFirstWarning(key)
          .getOrElse(
            getFirstInfo(key).orNull
          )
      )
    Option(v)
  }

  def getCauseCode(key: String, pathSuffix: Set[String] = Set(".v")): Option[ScaledaMessage] =
    getMessageData(key)
      .map(_._2)
      .map(_.toIndexedSeq.reverse)
      // exist file path that ends with suffix
      .flatMap(_.find(m => m.file.exists(f => pathSuffix.exists(f.endsWith))))

  def getDisplayName                                = ScaledaBundle.message("windows.tool.log.message.title")
  private var tabManager: Option[ConsoleTabManager] = None
  def setTabManager(p: Option[ConsoleTabManager])   = tabManager = p
  def getTabManager                                 = tabManager
  def switchToTab(): Unit = {
    tabManager.foreach(t => {
      if (!t.setSelectedContent(getDisplayName)) MainLogger.warn("Cannot switch to message tab!")
    })
  }

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
    ScaledaMessageRenderer.getRendererMap.getOrElse(toolchainType, ScaledaMessageRendererDefault)

  // Do sort here, filter is in other part
  private def sortData(): Unit = {
    if (sortByLevel) {
      dataModel.synchronized {
        val data = dataModel.toArray.map(_.asInstanceOf[ScaledaMessage])
        data.sortInPlaceWith((a, b) =>
          if (a.level.id == b.level.id) a.time.toEpochMilli < b.time.toEpochMilli else a.level.id > b.level.id
        )
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

  private val messageQueue = new LinkedBlockingQueue[(ScaledaRuntime, ScaledaMessage)]()

  private val messageHandleThread = new Thread(
    () => {
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
    },
    "message-tab-handler"
  )
  messageHandleThread.start()

  def attach(runtime: ScaledaRuntime): Unit = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.addListener(
      runtime.id,
      new ScaledaMessageParser(
        runtime,
        message => {
          messageQueue.put((runtime, message))
          MainLogger.info(s"[ RUNTIME: ${runtime.id} ] message insert:", message)
        }
      )
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
  listComponent.setCellRenderer(ScaledaMessageRendererDefault)

  private val cleanThisRunMessageAction = new AnAction(
    ScaledaBundle.message("windows.message.action.clean.short"),
    ScaledaBundle.message("windows.message.action.clean"),
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

  // remove message data in this select
  private val clearAllMessageAction = new AnAction(
    ScaledaBundle.message("windows.message.action.clear.short"),
    ScaledaBundle.message("windows.message.action.clear"),
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

  private def getToggleSortIcon: Icon =
    if (sortByLevel)
      AllIcons.RunConfigurations.Scroll_down
    else AllIcons.General.AutoscrollToSource
  private val toggleSortAction = new ToggleAction(
    ScaledaBundle.message("windows.message.action.sort.short"),
    ScaledaBundle.message("windows.message.action.sort"),
    AllIcons.ObjectBrowser.Sorted
  ) {
    override def isSelected(e: AnActionEvent): Boolean = sortByLevel
    override def setSelected(e: AnActionEvent, state: Boolean): Unit = {
      sortByLevel = state
      selectToViewById(viewComboBox.getItem)
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
    new ToggleAction(level.toString, level.toString, icon) {
      override def setSelected(e: AnActionEvent, state: Boolean) = {
        if (!state) enabledLevel.remove(level)
        else enabledLevel.add(level)
        selectToViewById(viewComboBox.getItem)
      }

      override def isSelected(e: AnActionEvent): Boolean = {
        if (enabledLevel.contains(level)) true else false
      }
    }
  })

  val group = new DefaultActionGroup()
  group.add(clearAllMessageAction)
  group.add(cleanThisRunMessageAction)
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

  ScaledaMessageTab.setInstance(this)(project = project)

  override def dispose() = {
    // ScaledaMessageTab.INSTANCE = null
    tabManager = None
    messageHandleThread.interrupt()
    val service = project.getService(classOf[ScaledaLoggingService])
    service.removeListener(msgSourceId)
    // ManifestManager.disposeProject(project)
    IdeaManifestManager.removeObject(ScaledaMessageTab.MESSAGE_ID)(project = project)
  }
}

object ScaledaMessageTab {
  val MESSAGE_ID = "scaleda-message"

  def instance(implicit project: Project): Option[ScaledaMessageTab] = IdeaManifestManager.getObject(MESSAGE_ID)

  private def setInstance(instance: ScaledaMessageTab)(implicit project: Project): Unit = {
    IdeaManifestManager.putObject(MESSAGE_ID, instance)
  }

  def apply(project: Project, tabManager: Option[ConsoleTabManager] = None) = {
    val searchInstance = instance(project = project)
    val ret            = searchInstance.getOrElse(new ScaledaMessageTab(project))
    if (tabManager.nonEmpty) ret.setTabManager(tabManager)
    ret
  }
}

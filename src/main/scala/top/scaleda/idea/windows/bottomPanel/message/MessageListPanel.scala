package top.scaleda
package idea.windows.bottomPanel.message

import idea.ScaledaBundle
import idea.utils.runInEdt
import kernel.utils.LogLevel

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.{JBList, JBScrollPane}

import javax.swing.{BoxLayout, DefaultListModel, JPanel}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.jdk.javaapi.CollectionConverters

class MessageListPanel(project: Project) extends SimpleToolWindowPanel(false, true) with Disposable {
  // register myself to list service
  private val service = project.getService(classOf[MessageListService])
  service.registerMessageTab(this)

  private var sortByLevel = false // false = sort by time; true = sort by level
  private val dataModel   = new DefaultListModel[ScaledaMessage]()
  private val messages    = ArrayBuffer[ScaledaMessage]()

  private val allLevels = Seq(
    LogLevel.Debug,
    LogLevel.Verbose,
    LogLevel.Info,
    LogLevel.Warn,
    LogLevel.Error,
    LogLevel.Fatal
  )
  private val enabledLevel = new mutable.HashSet[LogLevel.Value]()
  enabledLevel.addAll(allLevels.slice(allLevels.indexOf(LogLevel.Info), allLevels.size))

  private def findRenderer(toolchainType: String) =
    ScaledaMessageRenderer.getRendererMap.getOrElse(toolchainType, ScaledaMessageRendererDefault)

  private val clearMessageListAction = new AnAction(
    ScaledaBundle.message("windows.message.action.clear.short"),
    ScaledaBundle.message("windows.message.action.clear"),
    AllIcons.Actions.GC
  ) {
    override def actionPerformed(e: AnActionEvent): Unit = {
      dataModel.synchronized {
        dataModel.clear()
      }
      messages.synchronized {
        messages.clear()
      }
    }

    override def getActionUpdateThread: ActionUpdateThread = ActionUpdateThread.BGT
  }

  /** Refill data from [[messages]] to [[dataModel]]
    */
  private def refillData(): Unit = {
    val renderer = findRenderer(service.getCurrentRuntime.map(_.target.toolchain).orNull)
    listComponent.setCellRenderer(renderer)
    val newMessages = messages.synchronized {
      messages.filter(m => enabledLevel.contains(m.level))
    }
    dataModel.synchronized {
      dataModel.clear()
      dataModel.addAll(CollectionConverters.asJava(newMessages))
    }
  }

  /** Sort data already in [[dataModel]]
    */
  private def sortData(): Unit = {
    val data = dataModel.toArray.map(_.asInstanceOf[ScaledaMessage])
    data.sortInPlaceWith((a, b) =>
      if (sortByLevel) {
        if (a.level.id == b.level.id) a.time.toEpochMilli < b.time.toEpochMilli else a.level.id > b.level.id
      } else {
        // by time
        a.time.toEpochMilli < b.time.toEpochMilli
      }
    )
    dataModel.synchronized {
      dataModel.clear()
      dataModel.addAll(CollectionConverters.asJava(data))
    }
  }

  private val toggleSortAction = new ToggleAction(
    ScaledaBundle.message("windows.message.action.sort.short"),
    ScaledaBundle.message("windows.message.action.sort"),
    AllIcons.ObjectBrowser.Sorted
  ) {
    override def isSelected(e: AnActionEvent): Boolean = sortByLevel
    override def setSelected(e: AnActionEvent, state: Boolean): Unit = {
      sortByLevel = state
      sortData()
    }
    override def getActionUpdateThread: ActionUpdateThread = ActionUpdateThread.BGT
  }

  private val filterLevelActions = allLevels.map(level => {
    val levelIcons = Map(
      LogLevel.Debug   -> AllIcons.General.Note,
      LogLevel.Verbose -> AllIcons.Debugger.Db_muted_field_breakpoint,
      LogLevel.Info    -> AllIcons.General.Information,
      LogLevel.Warn    -> AllIcons.General.Warning,
      LogLevel.Error   -> AllIcons.General.Error,
      LogLevel.Fatal   -> AllIcons.Ide.FatalError
    )
    val icon = levelIcons(level)
    //noinspection ReferencePassedToNls
    new ToggleAction(level.toString, level.toString, icon) {
      override def setSelected(e: AnActionEvent, state: Boolean): Unit = {
        if (!state) enabledLevel.remove(level)
        else enabledLevel.add(level)
        // refill, and sort
        refillData()
        sortData()
      }

      override def isSelected(e: AnActionEvent): Boolean = {
        if (enabledLevel.contains(level)) true else false
      }

      override def getActionUpdateThread: ActionUpdateThread = ActionUpdateThread.BGT
    }
  })

  /** * DRAWING UI **
    */
  private val group = new DefaultActionGroup()
  group.add(clearMessageListAction)
  group.add(toggleSortAction)
  filterLevelActions.foreach(group.add)

  private val toolbar = ActionManager
    .getInstance()
    .createActionToolbar("ScaledaMessageToolbar", group, false)

  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)
  private val panel = new JPanel()
  panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS))

  private val listComponent = new JBList[ScaledaMessage](dataModel)
  private val scrollbar     = new JBScrollPane()
  listComponent.setAutoscrolls(false)
  listComponent.setCellRenderer(ScaledaMessageRendererDefault) // stub, will be replaced in [[refillData]]
  scrollbar.setViewportView(listComponent)
  panel.add(scrollbar)

  setContent(panel)

  def addMessage(message: ScaledaMessage): Unit = {
    messages.synchronized {
      messages.append(message)
    }
    runInEdt {
      refillData()
      sortData()
    }
  }

  def clearMessageList(): Unit = {
    runInEdt {
      ActionManager.getInstance().tryToExecute(clearMessageListAction, null, null, null, true)
    }
  }

  override def dispose(): Unit = {
    // TODO
  }

}

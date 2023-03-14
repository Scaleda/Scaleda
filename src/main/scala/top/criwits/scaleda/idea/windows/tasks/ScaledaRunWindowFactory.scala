package top.criwits.scaleda
package idea.windows.tasks

import idea.ScaledaBundle
import idea.runner.task.edit.{ScaledaCreateNewTargetAction, ScaledaCreateNewTaskAction}
import idea.runner.task.{ScaledaReloadTasksAction, ScaledaRunToolWindowTaskAction, ScaledaTaskPopupMenuAction}
import idea.utils.{MainLogger, ProjectNow, invokeLater}
import idea.windows.tasks.ScaledaRunWindowFactory.model
import kernel.project.config.{ProjectConfig, TaskConfig}

import com.intellij.execution.impl.RunManagerImpl
import com.intellij.ide.{CommonActionsManager, DefaultTreeExpander}
import com.intellij.openapi.actionSystem.CommonDataKeys._
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.treeStructure.Tree
import com.intellij.ui.{ScrollPaneFactory, TreeSpeedSearch}
import com.intellij.util.ui.tree.TreeUtil

import java.awt.GridLayout
import java.awt.event.{KeyEvent, MouseAdapter, MouseEvent}
import javax.swing.tree.DefaultTreeModel
import javax.swing.{JPanel, SwingUtilities}

class ScaledaRunWindowFactory extends ToolWindowFactory {
  private var created = false

  override def init(toolWindow: ToolWindow) = {
    super.init(toolWindow)
    MainLogger.info("ScaledaRunWindowFactory#init")
    invokeLater {
      ProjectNow().foreach(p => createToolWindowContent(p, toolWindow))
    }
  }

  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    if (created) return
    else created = true
    MainLogger.info("ScaledaRunWindowFactory#createToolWindowContent")
    toolWindow.setTitle(ScaledaBundle.message("tasks.tool.window.title"))
    val contentManager = toolWindow.getContentManager
    val options        = new ScaledaRunToolWindowOption(project)
    // DumbService
    //   .getInstance(project)
    //   .runWhenSmart(() => {
    invokeLater {
      model = Some(new DefaultTreeModel(null))
      val panel = new SimpleToolWindowPanel(true)
      val tree = new Tree(model.get) with DataProvider {
        override def getData(dataId: String): AnyRef = {
          if (
            /*PlatformCoreDataKeys.BGT_DATA_PROVIDER.is(dataId)*/
            PlatformCoreDataKeys.SLOW_DATA_PROVIDERS.is(dataId)
          ) {
            val selectedNodes =
              getSelectedNodes(
                classOf[ScaledaRunTaskNode],
                (_: ScaledaRunTaskNode) => true
              )
            slowData(dataId, selectedNodes)
          } else null
        }

        private def slowData(
            dataId: String,
            selectedNodes: Array[ScaledaRunTaskNode]
        ): TaskConfig = {
          ProjectConfig
            .getConfig()
            .map(c => {
              if (PSI_ELEMENT.is(dataId) && selectedNodes.nonEmpty) {
                val selected = selectedNodes.head
                c.taskByTaskTargetName(selected.name, selected.parent.get.name).map(t => t._2).orNull
              } else null
            })
            .orNull
        }
      }
      TreeUtil.installActions(tree)
      new TreeSpeedSearch(tree)
      panel.add(ScrollPaneFactory.createScrollPane(tree))
      // var autoScrollHandler = new AutoScrollTo

      // Run
      val runManager = RunManagerImpl.getInstanceImpl(project)
      val runTaskAction =
        new ScaledaRunToolWindowTaskAction(tree, project, runManager)
      runTaskAction.registerCustomShortcutSet(
        new CustomShortcutSet(KeyEvent.VK_ENTER),
        panel
      )
      tree.addMouseListener(new MouseAdapter {
        override def mousePressed(e: MouseEvent) = {
          if (e != null && e.getClickCount == 2 && e.getButton == MouseEvent.BUTTON1) {
            ActionManager
              .getInstance()
              .tryToExecute(runTaskAction, e, tree, "", true)
          }

        }
      })

      tree.addMouseListener(new MouseAdapter {
        override def mouseClicked(e: MouseEvent): Unit = {
          if (SwingUtilities.isRightMouseButton(e)) {
            ActionManager
              .getInstance()
              .tryToExecute(new ScaledaTaskPopupMenuAction(tree, e, runTaskAction, project), e, tree, null, true)
          }
        }
      })

      val group = new DefaultActionGroup()
      group.add(runTaskAction)

      group.addSeparator()

      val treeExpander = new DefaultTreeExpander(tree)
      val expandAll = CommonActionsManager
        .getInstance()
        .createExpandAllAction(treeExpander, tree)
      group.add(expandAll)
      ScaledaRunWindowFactory.expandAll = Some(expandAll)
      // auto expand all after creation
      ActionManager.getInstance().tryToExecute(expandAll, null, null, null, false)
      group.add(
        CommonActionsManager
          .getInstance()
          .createCollapseAllAction(treeExpander, tree)
      )

      group.addSeparator()

      val createTargetAction = new ScaledaCreateNewTargetAction(model.get, project)
      group.add(createTargetAction)
      val createTaskAction = new ScaledaCreateNewTaskAction(tree, project)
      group.add(createTaskAction)

      group.addSeparator()

      val refreshTasksAction = new ScaledaReloadTasksAction
      group.add(refreshTasksAction)

      val toolbar = ActionManager
        .getInstance()
        .createActionToolbar("ScaledaRunToolbar", group, true)
      toolbar.setTargetComponent(tree)
      val toolBarPanel     = new JPanel(new GridLayout())
      val toolbarComponent = toolbar.getComponent
      require(toolbarComponent != null)
      toolBarPanel.add(toolbarComponent)
      panel.setToolbar(toolBarPanel)
      contentManager.addContent(new ContentImpl(panel, "", true))
    }
    // })
  }
}

object ScaledaRunWindowFactory {
  val WINDOW_ID                       = ScaledaBundle.message("tasks.configuration.name")
  var model: Option[DefaultTreeModel] = None
  var expandAll: Option[AnAction]     = None

  def getRootNode: ScaledaRunRootNode = {
    ProjectConfig
      .getConfig()
      .map(c => {
        new ScaledaRunRootNode(
          c.name,
          c.targets.map(t => new ScaledaRunTargetNode(t)).toList
        )
      })
      .orNull
  }
}

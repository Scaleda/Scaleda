package top.scaleda
package idea.windows.rightPanel

import idea.runner.task.{ScaledaRunToolWindowTaskAction, ScaledaTaskPopupMenuAction}
import idea.windows.rightPanel.ip.ScaledaIPManageAction

import com.intellij.execution.impl.RunManagerImpl
import com.intellij.ide.{CommonActionsManager, DefaultTreeExpander}
import com.intellij.openapi.actionSystem.{ActionManager, CustomShortcutSet, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.{ScrollPaneFactory, TreeSpeedSearch, TreeUIHelper}
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import top.scaleda.idea.utils.ScaledaIdeaLogger
import top.scaleda.idea.windows.rightPanel.treeNodes.{ScaledaTasksDummyRootNode, ScaledaTasksRootNode}

import java.awt.event.{KeyEvent, MouseAdapter, MouseEvent}
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeModel}
import javax.swing.{BoxLayout, JPanel, SwingUtilities}
import scala.collection.mutable.ArrayBuffer

class ScaledaTasksPanel(project: Project) extends SimpleToolWindowPanel(true, true) {
  // register myself to tasks tree service
  private val service = project.getService(classOf[ScaledaTasksService])
  ScaledaIdeaLogger.info("ScaledaTasksPanel started")
  service.setPanel(this)

  // FIXME: not working...
  // private val dummyRoot: ScaledaTasksDummyRootNode = new ScaledaTasksDummyRootNode()
  private val model: DefaultTreeModel = new DefaultTreeModel(null)

  private val tree = new Tree(model)
  // tree.setRootVisible(false)
  tree.setRootVisible(true)
  tree.setCellRenderer(new ScaledaTasksTreeCellRenderer)

  TreeUtil.installActions(tree)

  locally {
    val _ = TreeUIHelper.getInstance().installTreeSpeedSearch(tree)
  }

  // Run
  private val runManager = RunManagerImpl.getInstanceImpl(project)
  private val runTaskAction =
    new ScaledaRunToolWindowTaskAction(tree, project, runManager)
  runTaskAction.registerCustomShortcutSet(
    new CustomShortcutSet(KeyEvent.VK_ENTER),
    this // need test!
  )

  // Double click
  tree.addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = {
      //noinspection DuplicatedCode
      if (e != null && e.getClickCount == 2 && e.getButton == MouseEvent.BUTTON1) {
        ActionManager
          .getInstance()
          .tryToExecute(runTaskAction, e, tree, null, true)
      }
    }
  })

  // Right click
  tree.addMouseListener(new MouseAdapter {
    override def mouseClicked(e: MouseEvent): Unit = {
      if (SwingUtilities.isRightMouseButton(e)) {
        ActionManager
          .getInstance()
          .tryToExecute(new ScaledaTaskPopupMenuAction(tree, e, runTaskAction, project), e, tree, null, true)
      }
    }
  })

  private val group = new DefaultActionGroup()
  group.add(runTaskAction)
  group.addSeparator()

  private val treeExpander = new DefaultTreeExpander(tree)
  group.add(
    CommonActionsManager
      .getInstance()
      .createExpandAllAction(treeExpander, tree)
  )
  group.add(
    CommonActionsManager
      .getInstance()
      .createCollapseAllAction(treeExpander, tree)
  )

  group.addSeparator()

  group.add(new ScaledaEditTasksAction)
  group.add(new ScaledaIPManageAction)

  group.addSeparator()

  private val refreshTasksAction = new ScaledaReloadTasksAction
  group.add(refreshTasksAction)

  private val toolbar = ActionManager
    .getInstance()
    .createActionToolbar("ScaledaTasksToolbar", group, true)
  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)

  add(ScrollPaneFactory.createScrollPane(tree))

  // Run reload action
  ActionManager.getInstance().tryToExecute(refreshTasksAction, null, null, null, true)

  def getTree: Tree              = tree
  def getModel: DefaultTreeModel = model

  // def getRoots: ArrayBuffer[ScaledaTasksRootNode] = dummyRoot.rootNodes
}

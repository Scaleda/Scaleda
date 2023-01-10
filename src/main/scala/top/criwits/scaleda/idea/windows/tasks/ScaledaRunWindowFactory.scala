package top.criwits.scaleda
package idea.windows.tasks

import idea.ScaledaBundle
import idea.runner.task.ScaledaRunToolWindowTaskAction
import kernel.project.config.ProjectConfig
import kernel.project.task.TaskConfig

import com.intellij.execution.impl.RunManagerImpl
import com.intellij.ide.{CommonActionsManager, DefaultTreeExpander}
import com.intellij.openapi.actionSystem.CommonDataKeys._
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.project.{DumbService, Project}
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.dsl.gridLayout.GridLayout
import com.intellij.ui.treeStructure.Tree
import com.intellij.ui.{ScrollPaneFactory, TreeSpeedSearch}
import com.intellij.util.ui.tree.TreeUtil

import java.awt.event.{KeyEvent, MouseAdapter, MouseEvent}
import javax.swing.JPanel
import javax.swing.tree.DefaultTreeModel

class ScaledaRunWindowFactory extends ToolWindowFactory {
  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    toolWindow.setTitle(ScaledaBundle.message("tasks.tool.window.title"))
    val contentManager = toolWindow.getContentManager
    val options = new ScaledaRunToolWindowOption(project)
    DumbService
      .getInstance(project)
      .runWhenSmart(() => {
        val model = new DefaultTreeModel(options.getRootNode)
        val panel = new SimpleToolWindowPanel(true)
        val tree = new Tree(model) with DataProvider {
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
                  c.taskByName(selected.name).map(t => t._2).orNull
                } else null
              })
              .orNull
          }
        }
        TreeUtil.installActions(tree)
        new TreeSpeedSearch(tree)
        panel.add(ScrollPaneFactory.createScrollPane(tree))
        val toolBarPanel = new JPanel(new GridLayout)
        // var autoScrollHandler = new AutoScrollTo
        val runManager = RunManagerImpl.getInstanceImpl(project)
        val runTaskAction =
          new ScaledaRunToolWindowTaskAction(tree, project, runManager)
        runTaskAction.registerCustomShortcutSet(
          new CustomShortcutSet(KeyEvent.VK_ENTER),
          panel
        )
        tree.addMouseListener(new MouseAdapter {
          override def mousePressed(e: MouseEvent) = {
            if (
              e != null & e.getClickCount == 2 && e.getButton == MouseEvent.BUTTON1
            ) {
              ActionManager
                .getInstance()
                .tryToExecute(runTaskAction, e, tree, "", true)
            }
          }
        })
        val group = new DefaultActionGroup()
        group.add(runTaskAction)
        group.addSeparator()
        val treeExpander = new DefaultTreeExpander(tree)
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
        // group.addSeparator()
        val toolbar = ActionManager
          .getInstance()
          .createActionToolbar("ScaledaRunToolbar", group, true)
        toolbar.setTargetComponent(tree)
        toolBarPanel.add(toolbar.getComponent)
        panel.setToolbar(toolBarPanel)
        contentManager.addContent(new ContentImpl(panel, "", true))
      })
  }
}

object ScaledaRunWindowFactory {
  val WINDOW_ID = "scaleda"
}

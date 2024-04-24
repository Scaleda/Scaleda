package top.scaleda
package idea.runner.task

import com.intellij.execution.impl.RunManagerImpl
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{ActionManager, ActionPlaces, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree
import idea.utils.ScaledaIdeaLogger
import top.scaleda.idea.windows.rightPanel.treeNodes.{ScaledaTasksRootNode, ScaledaTasksTargetNode, ScaledaTasksTaskNode}

import java.awt.Component
import java.awt.event.MouseEvent

class ScaledaTaskPopupMenuAction(
    tree: Tree,
    event: MouseEvent,
    runAction: AnAction,
    project: Project
) extends AnAction() {

  override def actionPerformed(e: AnActionEvent): Unit = {
    // prepare group
    val group = new DefaultActionGroup()

    // check which node is selected
    val rootNode = tree.getSelectedNodes(classOf[ScaledaTasksRootNode], (_: ScaledaTasksRootNode) => true)
    val targetNode = tree.getSelectedNodes(classOf[ScaledaTasksTargetNode], (_: ScaledaTasksTargetNode) => true)
    val taskNode = tree.getSelectedNodes(classOf[ScaledaTasksTaskNode], (_: ScaledaTasksTaskNode) => true)

//    if (targetNode.nonEmpty) {
//      group.add(new ScaledaEditTargetAction(targetNode.head.target, tree, project))
//    }

    if (taskNode.nonEmpty) {
      // root node
      group.add(runAction)
//      group.add(new ScaledaEditTaskAction(taskNode.head.task, tree, project))
    }

    val popupMenu = ActionManager.getInstance().createActionPopupMenu(ActionPlaces.POPUP, group)

    if (rootNode.nonEmpty || targetNode.nonEmpty || taskNode.nonEmpty) {
      popupMenu.getComponent.show(tree, event.getX, event.getY)
    }
  }
}

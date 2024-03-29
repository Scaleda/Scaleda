package top.scaleda
package idea.runner.task

import com.intellij.execution.impl.RunManagerImpl
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{ActionManager, ActionPlaces, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree
import idea.utils.MainLogger
import idea.windows.tasks.{ScaledaRunRootNode, ScaledaRunTargetNode, ScaledaRunTaskNode}

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
    val rootNode = tree.getSelectedNodes(classOf[ScaledaRunRootNode], (_: ScaledaRunRootNode) => true)
    val targetNode = tree.getSelectedNodes(classOf[ScaledaRunTargetNode], (_: ScaledaRunTargetNode) => true)
    val taskNode = tree.getSelectedNodes(classOf[ScaledaRunTaskNode], (_: ScaledaRunTaskNode) => true)

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

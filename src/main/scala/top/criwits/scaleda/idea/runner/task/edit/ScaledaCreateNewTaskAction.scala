package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{ActionManager, AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree
import top.criwits.scaleda.idea.runner.task.ScaledaReloadTasksAction
import top.criwits.scaleda.idea.runner.task.edit.task.EditTaskDialogWrapper
import top.criwits.scaleda.idea.windows.tasks.{ScaledaRunTargetNode, ScaledaRunTaskNode}
import top.criwits.scaleda.kernel.project.config.TargetConfig

class ScaledaCreateNewTaskAction(tree: Tree, project: Project)
    extends AnAction(
      ScaledaBundle.message("tasks.action.run.tool.window.create.task.name"),
      ScaledaBundle.message(
        "tasks.action.run.wool.window.create.task.description"
      ),
      AllIcons.Actions.AddList
    ) {

  private var target: Option[TargetConfig] = None

  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    // update task nodes
    val taskNodes = tree.getSelectedNodes(classOf[ScaledaRunTaskNode], (_: ScaledaRunTaskNode) => true)
    // update target nodes
    val targetNodes = tree.getSelectedNodes(classOf[ScaledaRunTargetNode], (_: ScaledaRunTargetNode) => true)
    target = if (taskNodes.nonEmpty) { Some(taskNodes.head.task.parentNode.get.asInstanceOf[TargetConfig]) } // FIXME: ?
    else if (targetNodes.nonEmpty) { Some(targetNodes.head.target) }
    else None
    presentation.setEnabled(target.isDefined)
  }

  override def actionPerformed(e: AnActionEvent) = {
    // when calling this function, target should never be None
    val _r = new EditTaskDialogWrapper(target.get, project, None).showAndGet()
  }
}

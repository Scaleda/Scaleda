package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle
import idea.runner.task.edit.task.EditTaskDialogWrapper
import idea.windows.tasks.ScaledaRunTaskNode
import kernel.project.config.{TargetConfig, TaskConfig}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree

class ScaledaEditTaskAction(
    task: TaskConfig,
    tree: Tree,
    project: Project
) extends AnAction(
      ScaledaBundle.message("windows.edit.task", task.name),
      ScaledaBundle.message("windows.edit.task", task.name),
      null
    ) {

  private var target: Option[TargetConfig] = None

  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    // update task nodes
    val taskNodes = tree.getSelectedNodes(classOf[ScaledaRunTaskNode], (_: ScaledaRunTaskNode) => true)
    target = if (taskNodes.nonEmpty) { Some(taskNodes.head.task.parentNode.get.asInstanceOf[TargetConfig]) }
    else None
    presentation.setEnabled(target.isDefined)
  }

  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new EditTaskDialogWrapper(target.get, project, Some(task)).showAndGet()
  }
}

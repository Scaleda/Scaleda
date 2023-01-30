package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle
import kernel.project.config.TargetConfig

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree
import top.criwits.scaleda.idea.windows.tasks.ScaledaRunTargetNode

class ScaledaEditTargetAction(
    target: TargetConfig,
    tree: Tree,
    project: Project
) extends AnAction(
      ScaledaBundle.message("windows.edit.target", target.name),
      ScaledaBundle.message("windows.edit.target", target.name),
      null
    ) {

  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    val targetNodes = tree.getSelectedNodes(classOf[ScaledaRunTargetNode], (_: ScaledaRunTargetNode) => true)
    presentation.setEnabled(targetNodes.nonEmpty)
  }

  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new EditTargetDialogWrapper(project, Some(target)).showAndGet()
  }
}

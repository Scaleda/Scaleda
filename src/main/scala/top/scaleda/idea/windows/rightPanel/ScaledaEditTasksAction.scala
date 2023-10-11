package top.scaleda
package idea.windows.rightPanel

import idea.ScaledaBundle

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import top.scaleda.idea.project.io.YmlRootManager

class ScaledaEditTasksAction
    extends AnAction(
      ScaledaBundle.message("windows.edit.tasks"),
      ScaledaBundle.message("windows.edit.tasks"),
      AllIcons.Actions.Edit
    ) {

  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    presentation.setEnabled(YmlRootManager.getInstance(e.getProject).getRoots.nonEmpty)
  }
  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new ScaledaEditTasksDialog(e.getProject).showAndGet()
  }
}

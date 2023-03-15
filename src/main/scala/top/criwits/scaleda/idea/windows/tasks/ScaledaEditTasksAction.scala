package top.criwits.scaleda
package idea.windows.tasks

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import top.criwits.scaleda.idea.ScaledaBundle

class ScaledaEditTasksAction extends AnAction(
  ScaledaBundle.message("windows.edit.tasks"),
  ScaledaBundle.message("windows.edit.tasks"),
  AllIcons.Actions.Edit
) {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new ScaledaEditTasksDialog(e.getProject).showAndGet()
  }
}

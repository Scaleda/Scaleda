package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle
import kernel.project.task.TargetConfig

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project

class ScaledaRunNewTargetAction(project: Project)
    extends AnAction(
      ScaledaBundle.message("tasks.action.run.tool.window.create.target.name"),
      ScaledaBundle.message(
        "tasks.action.run.wool.window.create.target.description"
      ),
      AllIcons.Actions.NewFolder
    ) {
  override def actionPerformed(e: AnActionEvent) = {
    val _r = new EditTargetDialogWrapper(project, TargetConfig()).showAndGet()
  }
}

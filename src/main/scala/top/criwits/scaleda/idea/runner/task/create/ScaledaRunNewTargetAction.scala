package top.criwits.scaleda
package idea.runner.task.create

import idea.ScaledaBundle

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import top.criwits.scaleda.idea.utils.MainLogger

class ScaledaRunNewTargetAction(project: Project)
    extends AnAction(
      ScaledaBundle.message("tasks.action.run.tool.window.create.target.name"),
      ScaledaBundle.message(
        "tasks.action.run.wool.window.create.target.description"
      ),
      AllIcons.Actions.NewFolder
    ) {
  override def actionPerformed(e: AnActionEvent) = {
    val r = new CreateTargetDialogWrapper(project).showAndGet()
    MainLogger.info("dialog returns", r)
    // val r = new CreateTargetDialog()
    // r.pack()
    // r.setVisible(true)
  }
}

package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

class ScaledaRunNewTaskAction
    extends AnAction(
      ScaledaBundle.message("tasks.action.run.tool.window.create.task.name"),
      ScaledaBundle.message("tasks.action.run.wool.window.create.task.description"),
      AllIcons.Actions.AddList
    ) {
  override def actionPerformed(e: AnActionEvent) = {}
}

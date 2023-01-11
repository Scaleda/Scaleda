package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

class ScaledaRunNewTargetAction
  extends AnAction(
    ScaledaBundle.message("tasks.action.run.tool.window.create.target.name"),
    ScaledaBundle.message("tasks.action.run.wool.window.create.target.description"),
    AllIcons.Actions.NewFolder
  ) {
  override def actionPerformed(e: AnActionEvent) = {}
}

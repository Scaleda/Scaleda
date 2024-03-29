package top.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.utils.{Icons, MainLogger}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import top.scaleda.kernel.project.config.{TargetConfig, TaskConfig}
import top.scaleda.kernel.utils.serialise.JSONHelper

class ScaledaRunLastTaskAction
  extends AnAction(ScaledaBundle.message("tasks.action.run.task.name",
    ScaledaRunLastTaskAction.lastRunTask.map(_.name).getOrElse("None")),
    ScaledaBundle.message("tasks.action.run.task.description",
      ScaledaRunLastTaskAction.lastRunTarget.map(_.name).getOrElse("None"),
      ScaledaRunLastTaskAction.lastRunTask.map(_.name).getOrElse("None")),
    Icons.mainSmall) {
  override def actionPerformed(e: AnActionEvent): Unit = {
    import ScaledaRunLastTaskAction._
    if (lastRunTarget.nonEmpty && lastRunTask.nonEmpty) {
      MainLogger.warn("starting target", JSONHelper(lastRunTarget.get), "task", JSONHelper(lastRunTask.get))
    }
  }
}

object ScaledaRunLastTaskAction {
  var lastRunTarget: Option[TargetConfig] = None
  var lastRunTask: Option[TaskConfig] = None
}
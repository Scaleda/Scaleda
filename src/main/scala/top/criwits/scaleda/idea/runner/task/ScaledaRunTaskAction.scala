package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.utils.{Icons, MainLogger}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import top.criwits.scaleda.kernel.project.config.{TargetConfig, TaskConfig}
import top.criwits.scaleda.kernel.utils.serialise.JSONHelper

class ScaledaRunTaskAction(target: TargetConfig, task: TaskConfig)
    extends AnAction(
      ScaledaBundle.message("tasks.action.run.task.name", task.name),
      ScaledaBundle
        .message("tasks.action.run.task.description", target.name, task.name),
      Icons.mainSmall
    ) {
  override def actionPerformed(e: AnActionEvent): Unit = {
    MainLogger.warn(s"starting task ${task.name}")
  }
}

package top.criwits.scaleda
package idea.runner.configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.project.Project
import top.criwits.scaleda.kernel.project.config.{TargetConfig, TaskConfig}

class ScaledaRunConfigurationFactory(
    private val runConfigurationType: ScaledaRunConfigurationType
) extends ConfigurationFactory(runConfigurationType) {
  override def createTemplateConfiguration(project: Project) =
    new ScaledaRunConfiguration(project, this, "name")

  override def getId = "Scaleda"

  override def getName = "SCALEDA_TASKS_RUN_CONFIGURATION_FACTORY"

  def createConfigurationFromTargetAndTask(
      project: Project,
      target: TargetConfig,
      task: TaskConfig
  ): ScaledaRunConfiguration = {
    val c = new ScaledaRunConfiguration(project, this, target.name + "-" + task.name)
    c.targetName = target.name
    c.taskName = task.name
    c
  }
}

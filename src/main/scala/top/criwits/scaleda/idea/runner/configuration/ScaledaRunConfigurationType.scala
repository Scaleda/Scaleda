package top.criwits.scaleda
package idea.runner.configuration

import idea.ScaledaBundle
import idea.utils.Icons

import com.intellij.execution.configurations.ConfigurationType

class ScaledaRunConfigurationType extends ConfigurationType {
  override def getDisplayName = ScaledaBundle.message("tasks.configuration.name")

  override def getConfigurationTypeDescription = ScaledaBundle.message("tasks.configuration.description")

  override def getIcon = Icons.mainSmall

  override def getId = "SCALEDA_TASKS_RUN_CONFIGURATION"

  override def getConfigurationFactories = Array(new ScaledaRunConfigurationFactory(this))
}

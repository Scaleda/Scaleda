package top.criwits.scaleda
package idea.runner.configuration

import com.intellij.execution.configurations.ConfigurationType
import top.criwits.scaleda.idea.utils.Icons

class ScaledaRunConfigurationType extends ConfigurationType {
  override def getDisplayName = "Scaleda Tasks"

  override def getConfigurationTypeDescription = "Run Scaleda task"

  override def getIcon = Icons.mainSmall

  override def getId = "SCALEDA_TASKS_RUN_CONFIGURATION"

  override def getConfigurationFactories = Array(new ScaledaRunConfigurationFactory(this))
}

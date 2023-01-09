package top.criwits.scaleda
package idea.runner.configuration

import com.intellij.execution.actions.{ConfigurationContext, LazyRunConfigurationProducer}
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import top.criwits.scaleda.kernel.project.config.ProjectConfig

class ScaledaRunConfigurationProducer extends LazyRunConfigurationProducer[ScaledaRunConfiguration]{
  override def setupConfigurationFromContext(configuration: ScaledaRunConfiguration, context: ConfigurationContext, sourceElement: Ref[PsiElement]): Boolean = {
    ProjectConfig.getConfig().exists(c => {
      c.headTarget.foreach(t => configuration.targetName = t.name)
      c.headTask.foreach(t => configuration.taskName = t.name)
      true
    })
  }

  override def isConfigurationFromContext(configuration: ScaledaRunConfiguration, context: ConfigurationContext): Boolean = {
    ProjectConfig.getConfig().exists(c => {
      c.targets.find(_.name == configuration.targetName)
        .map(target => target.tasks.find(_.name == configuration.taskName)).exists(_.nonEmpty)
    })
  }

  override def getConfigurationFactory: ConfigurationFactory = new ScaledaRunConfigurationFactory(ScaledaRunConfigurationType.instance)
}
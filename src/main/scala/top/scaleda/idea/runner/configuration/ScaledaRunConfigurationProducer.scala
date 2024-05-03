package top.scaleda
package idea.runner.configuration

import idea.utils.ScaledaIdeaLogger
import kernel.project.config.ProjectConfig

import com.intellij.execution.actions.{ConfigurationContext, LazyRunConfigurationProducer}
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import top.scaleda.idea.project.io.YmlRootManager
import top.scaleda.kernel.project.ScaledaProject

class ScaledaRunConfigurationProducer extends LazyRunConfigurationProducer[ScaledaRunConfiguration] {
  override def setupConfigurationFromContext(
      configuration: ScaledaRunConfiguration,
      context: ConfigurationContext,
      sourceElement: Ref[PsiElement]
  ): Boolean = {
    ScaledaIdeaLogger.debug("setupConfigurationFromContext config taskName:", configuration.taskName)
    YmlRootManager.getInstance(context.getProject).getRoots.headOption.map(_.toScaledaProject) map { implicit project =>
      ProjectConfig.getConfig
        .map(c => {
          c.headTarget.foreach(t => configuration.targetName = t.name)
          c.headTask.foreach(t => configuration.taskName = t.name)
          true
        })
        .getOrElse({
          ScaledaIdeaLogger.debug("cannot load config when setting up configurations")
          false
        })
    } getOrElse {
      false
    }
  }

  override def isConfigurationFromContext(
      configuration: ScaledaRunConfiguration,
      context: ConfigurationContext
  ): Boolean = {
    YmlRootManager.getInstance(context.getProject).getRoots.headOption.map(_.toScaledaProject) map { implicit project =>
      ProjectConfig.getConfig
        .exists(c => {
          c.targets
            .find(_.name == configuration.targetName)
            .map(target => target.tasks.find(_.name == configuration.taskName))
            .exists(_.nonEmpty)
        })
    } getOrElse {
      false
    }
  }

  override def getConfigurationFactory: ConfigurationFactory = new ScaledaRunConfigurationFactory(
    ScaledaRunConfigurationType.instance
  )
}

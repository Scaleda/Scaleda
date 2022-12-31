package top.criwits.scaleda
package idea.panel

import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.service.task.ui.AbstractExternalSystemToolWindowFactory
import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemSettings
import com.intellij.openapi.project.Project
import top.criwits.scaleda.idea.config.{ScaledaProjectSettings, ScaledaProjectSettingsListener, ScaledaSettings}

class ScaledaToolWindowFactory extends AbstractExternalSystemToolWindowFactory(/* TODO */ new ProjectSystemId("Scaleda")) {
  override def getSettings(project: Project): AbstractExternalSystemSettings[ScaledaSettings, ScaledaProjectSettings, ScaledaProjectSettingsListener] = null
}

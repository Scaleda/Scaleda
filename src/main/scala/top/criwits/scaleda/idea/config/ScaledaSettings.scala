package top.criwits.scaleda
package idea.config

import com.intellij.openapi.components.{PersistentStateComponent, RoamingType, State, Storage}
import com.intellij.openapi.externalSystem.settings.{AbstractExternalSystemSettings, ExternalSystemSettingsListener}
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic

@State(
  name = "ScaledaSettings",
  storages = Array(new Storage(value = "scaleda.xml", roamingType = RoamingType.DISABLED)),
  reportStatistic = true
)
class ScaledaSettings(project: Project)
  extends AbstractExternalSystemSettings
    [ScaledaSettings, ScaledaProjectSettings, ScaledaProjectSettingsListener](ScaledaSettings.ScaledaTopic, project)
  with PersistentStateComponent[ScaledaSettings] {
  override def subscribe(listener: ExternalSystemSettingsListener[ScaledaProjectSettings]): Unit = {

  }

  override def copyExtraSettingsFrom(settings: ScaledaSettings): Unit = {

  }

  override def checkSettings(old: ScaledaProjectSettings, current: ScaledaProjectSettings): Unit = {

  }

  override def getState: ScaledaSettings = {
    // TODO
    this
  }

  override def loadState(state: ScaledaSettings): Unit = {
    // TODO
  }
}

object ScaledaSettings {
  val ScaledaTopic = new Topic("scaleda settings", classOf[ScaledaProjectSettingsListener]);

}
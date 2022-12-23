package top.criwits.scaleda
package config

import com.intellij.openapi.application.{Application, ApplicationManager}
import com.intellij.openapi.components.{PersistentStateComponent, ServiceManager, State, Storage}
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
  name = "top.criwits.scaleda.EDAProfiles",
  storages = Array(new Storage("eda_profile.xml"))
)
class EDAProfileState extends PersistentStateComponent[EDAProfileState] {
  // Profiles
  val profiles: Seq[EDAProfile] = Seq.empty

  override def getState: EDAProfileState = this

  override def loadState(state: EDAProfileState): Unit = XmlSerializerUtil.copyBean(state, this)
}

object EDAProfileState {
  def getInstance: EDAProfileState = ApplicationManager.getApplication
    .getService(EDAProfileState.getClass[EDAProfileState])
}

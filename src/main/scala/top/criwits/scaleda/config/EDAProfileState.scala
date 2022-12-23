package top.criwits.scaleda
package config

import com.intellij.openapi.components.{PersistentStateComponent, State, Storage}

@State(
  name = "top.criwits.scaleda.EDAProfiles",
  storages = Array(new Storage("eda_profile.xml"))
)
class EDAProfileState extends PersistentStateComponent[EDAProfileState] {
  override def getState: EDAProfileState = ???

  override def loadState(state: EDAProfileState): Unit = ???
}

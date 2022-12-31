package top.criwits.scaleda
package kernel.toolchain.config

import com.intellij.openapi.application.{Application, ApplicationManager}
import com.intellij.openapi.components.{PersistentStateComponent, ServiceManager, State, Storage}
import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemSettings
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.XCollection
import top.criwits.scaleda.kernel.toolchain.ToolchainProfile

import java.util
import scala.beans.BeanProperty

@State(name = "scaleda.toolchain.profiles", storages = Array(new Storage("scaleda-toolchains.xml")))
class ToolchainProfiles extends PersistentStateComponent[ToolchainProfiles.State] {
  // Profiles
  @BeanProperty
  var profiles: ToolchainProfiles.State = new ToolchainProfiles.State

  override def getState: ToolchainProfiles.State = profiles

  override def loadState(state: ToolchainProfiles.State): Unit = {
    profiles = state
  }
}

object ToolchainProfiles {
  class State extends AbstractExternalSystemSettings.State[ToolchainProfile] {
    private val toolchainProfiles = new util.TreeSet[ToolchainProfile]

    @XCollection(style = XCollection.Style.v1, elementTypes = Array(classOf[ToolchainProfile]))
    override def getLinkedExternalProjectsSettings: util.Set[ToolchainProfile] = toolchainProfiles

    override def setLinkedExternalProjectsSettings(settings: util.Set[ToolchainProfile]): Unit = {
      toolchainProfiles.addAll(settings)
    }
  }
  def getInstance: ToolchainProfiles = ApplicationManager.getApplication
    .getService(classOf[ToolchainProfiles])
}

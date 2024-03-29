package top.scaleda
package idea.application.config

import idea.waveform.RvcdHandler
import kernel.utils.Paths
import kernel.utils.serialise.YAMLHelper

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

import java.io.File

@JsonInclude(Include.NON_EMPTY)
case class ScaledaIdeaConfig(
    autoOpenWaveform: Boolean = true,
    notifyWaveformUpdate: Boolean = true,
    waveformHandler: String = RvcdHandler.getId
)

object ScaledaIdeaConfig {
  private val configFile = new File(Paths.getGlobalConfigDir, "config.yml")
  def getConfig: ScaledaIdeaConfig = {
    val doLoadFile = () => YAMLHelper(configFile, classOf[ScaledaIdeaConfig])
    try {
      doLoadFile()
    } catch {
      case e: Throwable =>
        saveConfig(ScaledaIdeaConfig())
        doLoadFile()
    }
  }
  def saveConfig(config: ScaledaIdeaConfig): Unit = YAMLHelper(config, configFile)
}

package top.scaleda
package idea.application.config

import idea.lsp.LspServers.VeribleLspServer
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
    waveformHandler: String = RvcdHandler.getId,
    lsp: ScaledaLspConfig = ScaledaLspConfig()
)

@JsonInclude(Include.NON_EMPTY)
case class ScaledaLspConfig(
    tool: String = VeribleLspServer.name,
    generateFileLists: Boolean = true,
    path: Map[String, String] = Map()
)

// TODO: use IDEA PersistentStateComponent instead
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

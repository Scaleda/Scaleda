package top.scaleda
package idea.settings.generic

import idea.ScaledaBundle
import idea.application.config.ScaledaIdeaConfig
import idea.waveform.WaveformHandler

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.FormBuilder

import javax.swing.{JCheckBox, JComponent, JPanel}

class ScaledaGenericConfigurable extends SearchableConfigurable {
  override def getId: String = ScaledaGenericConfigurable.SETTINGS_ID

  override def getDisplayName: String = ScaledaBundle.message("settings.generic")

  var config: ScaledaIdeaConfig = ScaledaIdeaConfig.getConfig

  val autoOpenWaveform     = new JCheckBox()
  val notifyWaveformUpdate = new JCheckBox()
  val waveformHandler      = new ComboBox[String]()
  WaveformHandler.availableHandlers.foreach(t => waveformHandler.addItem(t._1))

  override def reset() = {
    config = ScaledaIdeaConfig.getConfig
    autoOpenWaveform.setSelected(config.autoOpenWaveform)
    notifyWaveformUpdate.setSelected(config.notifyWaveformUpdate)
    waveformHandler.setItem(config.waveformHandler)
  }
  reset()

  private val mainPanel = new JPanel()
  mainPanel.add(
    FormBuilder
      .createFormBuilder()
      // .setAlignLabelOnRight(false)
      // .setHorizontalGap(UIUtil.DEFAULT_HGAP)
      // .setVerticalGap(UIUtil.DEFAULT_VGAP)
      .addLabeledComponent(ScaledaBundle.message("settings.generic.auto_open_waveform"), autoOpenWaveform)
      .addLabeledComponent(ScaledaBundle.message("settings.generic.notify_waveform_update"), notifyWaveformUpdate)
      .addLabeledComponent(ScaledaBundle.message("settings.generic.waveform_handler"), waveformHandler)
      .getPanel
  )

  override def createComponent(): JComponent = mainPanel

  private def dumpToConfig = config.copy(
    autoOpenWaveform = autoOpenWaveform.isSelected,
    notifyWaveformUpdate = notifyWaveformUpdate.isSelected,
    waveformHandler = waveformHandler.getItem
  )

  override def isModified: Boolean = dumpToConfig != ScaledaIdeaConfig.getConfig

  override def apply(): Unit = {
    ScaledaIdeaConfig.saveConfig(dumpToConfig)
  }
}

object ScaledaGenericConfigurable {
  val SETTINGS_ID = "top.scaleda.idea.settings.generic"
}

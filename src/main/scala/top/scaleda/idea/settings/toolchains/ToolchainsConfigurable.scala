package top.scaleda
package idea.settings.toolchains

import com.intellij.openapi.options.SearchableConfigurable
import top.scaleda.idea.ScaledaBundle
import top.scaleda.kernel.toolchain.Toolchain

import javax.swing.JComponent

class ToolchainsConfigurable
  extends SearchableConfigurable {
  private val configUI = new ToolchainsPanel
  override def getId: String = ToolchainsConfigurable.SETTINGS_ID
  override def getDisplayName: String = ScaledaBundle.message("settings.toolchains")

  override def createComponent(): JComponent = configUI

  override def isModified: Boolean = configUI.modified()

  override def reset(): Unit = {
    configUI.reset()
    super.reset()
  }
  override def apply(): Unit = {
    configUI.apply()
  }

}

object ToolchainsConfigurable {
  val SETTINGS_ID = "top.scaleda.idea.settings.toolchains.ToolchainsConfigurable"
}
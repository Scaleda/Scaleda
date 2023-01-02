package top.criwits.scaleda
package idea.settings.toolchains

import com.intellij.openapi.options.SearchableConfigurable
import top.criwits.scaleda.idea.ScaledaBundle

import javax.swing.JComponent

class ToolchainsConfigurable
  extends SearchableConfigurable {
  private val configUI = new ToolchainsPanel
  override def getId: String = "top.criwits.scaleda.idea.settings.toolchains.ToolchainsConfigurable"
  override def getDisplayName: String = ScaledaBundle.message("settings.toolchains")

  override def createComponent(): JComponent = configUI

  override def isModified: Boolean = true

  override def apply(): Unit = {
    println("hey")
  }
}

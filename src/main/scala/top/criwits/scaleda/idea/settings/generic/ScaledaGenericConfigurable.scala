package top.criwits.scaleda
package idea.settings.generic

import com.intellij.openapi.options.SearchableConfigurable
import top.criwits.scaleda.idea.ScaledaBundle

import javax.swing.{JComponent, JPanel}

class ScaledaGenericConfigurable extends SearchableConfigurable {
  override def getId: String = ScaledaGenericConfigurable.SETTINGS_ID

  override def getDisplayName: String = ScaledaBundle.message("settings.generic")

  override def createComponent(): JComponent = new JPanel()

  override def isModified: Boolean = false

  override def apply(): Unit = {}
}

object ScaledaGenericConfigurable {
  val SETTINGS_ID = "top.criwits.scaleda.idea.settings.generic"
}
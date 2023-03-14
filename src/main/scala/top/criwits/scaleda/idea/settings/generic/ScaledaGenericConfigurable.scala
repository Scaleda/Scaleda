package top.criwits.scaleda
package idea.settings.generic

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.util.ui.{FormBuilder, UIUtil}
import top.criwits.scaleda.idea.ScaledaBundle

import javax.swing.{JComponent, JPanel}

class ScaledaGenericConfigurable extends SearchableConfigurable {
  override def getId: String = ScaledaGenericConfigurable.SETTINGS_ID

  override def getDisplayName: String = ScaledaBundle.message("settings.generic")

  override def createComponent(): JComponent = FormBuilder
    .createFormBuilder()
    .setAlignLabelOnRight(false)
    .setHorizontalGap(UIUtil.DEFAULT_HGAP)
    .setVerticalGap(UIUtil.DEFAULT_VGAP)
    // .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.target.name"), targetName)
    // .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.task.name"), taskName)
    // .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.profile.name"), profileName)
    // .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.profile.host"), profileHost)
    // .addComponentToRightColumn(profileStateComp)
    // .addComponent(environmentVarsComponent)
    .getPanel

  override def isModified: Boolean = false

  override def apply(): Unit = {}
}

object ScaledaGenericConfigurable {
  val SETTINGS_ID = "top.criwits.scaleda.idea.settings.generic"
}
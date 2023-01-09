package top.criwits.scaleda
package idea.runner.configuration

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.ui.{FormBuilder, UIUtil}

class ScaledaRunConfigurationEditor(private val project: Project)
  extends SettingsEditor[ScaledaRunConfiguration] {
  private val panel = FormBuilder.createFormBuilder()
    .setAlignLabelOnRight(false)
    .setHorizontalGap(UIUtil.DEFAULT_HGAP)
    .setVerticalGap(UIUtil.DEFAULT_VGAP)
    .getPanel

  override def resetEditorFrom(s: ScaledaRunConfiguration) = {}

  override def applyEditorTo(s: ScaledaRunConfiguration) = {}

  override def createEditor() = panel
}

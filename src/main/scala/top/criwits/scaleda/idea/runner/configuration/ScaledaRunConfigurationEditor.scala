package top.criwits.scaleda
package idea.runner.configuration

import idea.ScaledaBundle

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.util.ui.{FormBuilder, UIUtil}

class ScaledaRunConfigurationEditor(private val project: Project)
  extends SettingsEditor[ScaledaRunConfiguration] {

  private val taskName = new TextFieldWithBrowseButton

  private val panel = FormBuilder.createFormBuilder()
    .setAlignLabelOnRight(false)
    .setHorizontalGap(UIUtil.DEFAULT_HGAP)
    .setVerticalGap(UIUtil.DEFAULT_VGAP)
    .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.name"), taskName)
    .getPanel

  override def resetEditorFrom(s: ScaledaRunConfiguration) = {
    taskName.setText(s.taskName)
  }

  override def applyEditorTo(s: ScaledaRunConfiguration) = {
    s.taskName = taskName.getText
  }

  override def createEditor() = panel
}

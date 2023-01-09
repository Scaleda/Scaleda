package top.criwits.scaleda
package idea.runner.configuration

import idea.ScaledaBundle
import kernel.project.config.ProjectConfig

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.{FormBuilder, UIUtil}

import java.awt.event.ItemEvent
import scala.jdk.javaapi.CollectionConverters

class ScaledaRunConfigurationEditor(private val project: Project)
  extends SettingsEditor[ScaledaRunConfiguration] {

  private val targetName = new ComboBox[String]

  private val taskName = new ComboBox[String]

  private val environmentVarsComponent = new EnvironmentVariablesComponent

  ProjectConfig.headTarget.foreach(t => targetName.addItem(t.name))
  ProjectConfig.headTask.foreach(t => taskName.addItem(t.name))

  targetName.addItemListener(e => {
    if (e.getStateChange == ItemEvent.SELECTED) {
      val newTargetName = e.getItem.asInstanceOf[String]
      taskName.removeAllItems()
      ProjectConfig.getConfig().foreach(c => c.targets.find(_.name == newTargetName).foreach(t =>
        t.tasks.foreach(t => taskName.addItem(t.name))))
    }
  })

  private val panel = FormBuilder.createFormBuilder()
    .setAlignLabelOnRight(false)
    .setHorizontalGap(UIUtil.DEFAULT_HGAP)
    .setVerticalGap(UIUtil.DEFAULT_VGAP)
    .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.target.name"), targetName)
    .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.task.name"), taskName)
    .addComponent(environmentVarsComponent)
    .getPanel

  override def resetEditorFrom(s: ScaledaRunConfiguration) = {
    taskName.setItem(s.taskName)
    targetName.setItem(s.targetName)
    environmentVarsComponent.setEnvs(CollectionConverters.asJava(s.extraEnvs))
  }

  override def applyEditorTo(s: ScaledaRunConfiguration) = {
    s.taskName = taskName.getItem
    s.targetName = targetName.getItem
    s.extraEnvs.addAll(CollectionConverters.asScala(environmentVarsComponent.getEnvs))
  }

  override def createEditor() = panel
}
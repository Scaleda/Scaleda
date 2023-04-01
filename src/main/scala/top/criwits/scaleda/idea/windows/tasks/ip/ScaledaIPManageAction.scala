package top.criwits.scaleda
package idea.windows.tasks.ip

import idea.ScaledaBundle
import kernel.project.config.ProjectConfig

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

class ScaledaIPManageAction
    extends AnAction(
      ScaledaBundle.message("windows.ip.manager.title"),
      ScaledaBundle.message("windows.ip.manager.description"),
      AllIcons.Actions.Colors
    ) {
  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    presentation.setEnabled(ProjectConfig.projectBase.isDefined)
  }
  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new ScaledaIPManagerDialog(e.getProject).showAndGet()
  }
}

package top.scaleda
package idea.windows.rightPanel.ip

import idea.ScaledaBundle
import kernel.project.ManifestManager

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import top.scaleda.idea.project.IdeaManifestManager

class ScaledaIPManageAction
    extends AnAction(
      ScaledaBundle.message("windows.ip.manager.title"),
      ScaledaBundle.message("windows.ip.manager.description"),
      AllIcons.Actions.Colors
    ) {
  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    val manifest     = IdeaManifestManager.getImplicitManifest(project = e.getProject)
    presentation.setEnabled(manifest.projectBase.isDefined)
  }
  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new ScaledaIPManagerDialog(e.getProject).showAndGet()
  }
}

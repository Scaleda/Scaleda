package top.scaleda
package idea.windows.rightPanel

import idea.ScaledaBundle
import idea.project.IdeaManifestManager

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

class ScaledaEditTasksAction
    extends AnAction(
      ScaledaBundle.message("windows.edit.tasks"),
      ScaledaBundle.message("windows.edit.tasks"),
      AllIcons.Actions.Edit
    ) {

  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    val manifest     = IdeaManifestManager.getImplicitManifest(project = e.getProject)
    presentation.setEnabled(manifest.projectBase.isDefined)
  }
  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new ScaledaEditTasksDialog(e.getProject).showAndGet()
  }
}

package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle

import com.intellij.openapi.project.Project

import javax.swing.JPanel

class EditTargetDialogWrapper(project: Project)
    extends EditDialogWrapper(
      project,
      ScaledaBundle.message("windows.edit.target.title")
    ) {
  val inner = new EditTargetDialog

  override def getMainPanel: JPanel = inner.getMainPanel
}

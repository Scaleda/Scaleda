package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle

import com.intellij.openapi.project.Project

import javax.swing.JPanel

class EditTaskDialogWrapper(project: Project)
  extends EditDialogWrapper(
    project,
    ScaledaBundle.message("windows.edit.task.title")
  ) {
  val inner = new EditTaskDialog

  override def getMainPanel: JPanel = inner.getMainPanel
}

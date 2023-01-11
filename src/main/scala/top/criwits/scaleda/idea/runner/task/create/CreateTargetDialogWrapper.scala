package top.criwits.scaleda
package idea.runner.task.create

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

import javax.swing.{JComponent, JPanel}

class CreateTargetDialogWrapper(project: Project) extends DialogWrapper(project) {
  init()
  setTitle("Creating Scaleda Target")

  override def createCenterPanel(): JComponent = {
    val panel = new JPanel
    val inner = new EditTargetDialog
    panel.add(inner.getMainPanel)
    panel
  }
}

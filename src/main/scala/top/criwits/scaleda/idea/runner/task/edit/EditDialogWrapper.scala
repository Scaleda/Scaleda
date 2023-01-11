package top.criwits.scaleda
package idea.runner.task.edit

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

import javax.swing.{JComponent, JPanel}

abstract class EditDialogWrapper[T](
    project: Project,
    title: String
) extends DialogWrapper(project) with EditDialogProvider[T] {
  init()
  setTitle(title)

  override def createCenterPanel(): JComponent = {
    val panel = new JPanel
    panel.add(getMainPanel)
    panel
  }
}

package top.criwits.scaleda
package idea.runner.task.edit

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

import javax.swing.{JComponent, JPanel}

abstract class EditDialogWrapper[T](
    project: Project,
    title: String
) extends DialogWrapper(project) {
  init()
  setTitle(title)

  def createMainPanel: JPanel

  override def createCenterPanel(): JComponent = createMainPanel
}

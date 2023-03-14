package top.criwits.scaleda
package idea.windows.tasks

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import top.criwits.scaleda.idea.ScaledaBundle

import javax.swing.JComponent

class ScaledaEditTasksDialog(project: Project) extends DialogWrapper(project) {
  init()
  setTitle(ScaledaBundle.message("windows.edit"))

  override def createCenterPanel(): JComponent = ???

  override def doOKAction(): Unit = ???
}

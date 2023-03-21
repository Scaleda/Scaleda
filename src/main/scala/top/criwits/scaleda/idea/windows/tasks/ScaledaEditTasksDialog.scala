package top.criwits.scaleda
package idea.windows.tasks

import idea.ScaledaBundle
import kernel.project.config.ProjectConfig

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

import javax.swing.JComponent

class ScaledaEditTasksDialog(project: Project) extends DialogWrapper(project) {
  init()
  setTitle(ScaledaBundle.message("windows.edit"))
  setSize(800, 600)
  setResizable(true)

  var mainPanel: ScaledaEditTasksPanel = _

  override def createCenterPanel(): JComponent = {
    // get root node
    // TODO: if there're no project config?
    val rootNode = new ScaledaRunRootNode(ProjectConfig.getConfig().get)
    if (mainPanel == null) mainPanel = new ScaledaEditTasksPanel(rootNode, setValid)
    mainPanel
  }
  override def doOKAction(): Unit = {}

  private def setValid(ok: Boolean) = {
    setOKActionEnabled(ok)
  }

}

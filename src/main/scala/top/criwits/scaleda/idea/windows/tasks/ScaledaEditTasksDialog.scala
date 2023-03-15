package top.criwits.scaleda
package idea.windows.tasks

import com.intellij.CommonBundle
import com.intellij.openapi.options.ex.SingleConfigurableEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.kernel.project.config.ProjectConfig

import java.awt.event.ActionEvent
import java.util
import java.util.{ArrayList, List}
import javax.swing.{AbstractAction, Action, JComponent}

class ScaledaEditTasksDialog(project: Project) extends DialogWrapper(project) {
  init()
  setTitle(ScaledaBundle.message("windows.edit"))
  setSize(600, 400)
  setResizable(true)

  var mainPanel: ScaledaEditTasksPanel = _

  override def createCenterPanel(): JComponent = {
    val rootNode = new ScaledaRunRootNode(ProjectConfig.getConfig().get)
    if (mainPanel == null) mainPanel = new ScaledaEditTasksPanel(rootNode, setValid)
    mainPanel
  }
  override def doOKAction(): Unit = {}

  private def setValid(ok: Boolean) = {
    setOKActionEnabled(ok)
  }

}

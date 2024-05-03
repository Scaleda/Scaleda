package top.scaleda
package idea.windows.rightPanel

import idea.ScaledaBundle
import idea.project.io.YmlRootManager
import idea.windows.rightPanel.treeNodes.ScaledaTasksRootNode
import kernel.project.ScaledaProject
import kernel.project.config.ProjectConfig

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

import java.io.File
import javax.swing.JComponent

class ScaledaEditTasksDialog(project: Project) extends DialogWrapper(project) {
  init()
  setTitle(ScaledaBundle.message("windows.edit"))
  setSize(800, 600)
  setResizable(true)

  var mainPanel: ScaledaEditTasksPanel = _

  override def createCenterPanel(): JComponent = {
    YmlRootManager.getInstance(project).getRoots.headOption.map(_.toScaledaProject) map { implicit p =>
      ProjectConfig.getConfig
        .map(c => {
          val rootNode = new ScaledaTasksRootNode(c)
          if (mainPanel == null) mainPanel = new ScaledaEditTasksPanel(project, rootNode, setValid)
          mainPanel
        })
        .orNull
    } getOrElse {
      null
    }
  }
  override def doOKAction(): Unit = {
    YmlRootManager.getInstance(project).getRoots.headOption.map(_.toScaledaProject) map { implicit project =>
      ProjectConfig.saveConfig(
        mainPanel.scaledaRunRootNode.toProjectConfig,
        targetFile = new File(project.configFile.get)
      )
      ActionManager.getInstance().tryToExecute(new ScaledaReloadTasksAction, null, null, null, true)
    }
    super.doOKAction()
  }

  private def setValid(ok: Boolean) = {
    setOKActionEnabled(ok)
  }

}

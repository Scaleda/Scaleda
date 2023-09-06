package top.scaleda
package idea.windows.rightPanel

import idea.ScaledaBundle
import idea.project.IdeaManifestManager
import kernel.project.ManifestManager
import kernel.project.config.ProjectConfig

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import top.scaleda.idea.windows.rightPanel.treeNodes.ScaledaTasksRootNode

import java.io.File
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
    implicit val manifest = IdeaManifestManager.getImplicitManifest(project = project)
    ProjectConfig.getConfig
      .map(c => {
        val rootNode = new ScaledaTasksRootNode(c)
        if (mainPanel == null) mainPanel = new ScaledaEditTasksPanel(project, rootNode, setValid)
        mainPanel
      })
      .orNull
  }
  override def doOKAction(): Unit = {
    ProjectConfig.saveConfig(
      mainPanel.scaledaRunRootNode.toProjectConfig,
      targetFile = new File(ManifestManager.getManifest(project = project).configFile.get)
    )
    ActionManager.getInstance().tryToExecute(new ScaledaReloadTasksAction, null, null, null, true)
    super.doOKAction()
  }

  private def setValid(ok: Boolean) = {
    setOKActionEnabled(ok)
  }

}

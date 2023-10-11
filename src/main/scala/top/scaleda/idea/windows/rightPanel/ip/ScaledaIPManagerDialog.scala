package top.scaleda
package idea.windows.rightPanel.ip

import idea.ScaledaBundle
import kernel.project.config.ProjectConfig
import kernel.utils.KernelFileUtils

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import top.scaleda.idea.project.io.YmlRootManager
import top.scaleda.kernel.project.ScaledaProject

import java.awt.AWTEvent
import java.io.File
import javax.swing.JComponent

/** Dialog to manage IPs in the Scaleda project
  * @param project the project
  */
class ScaledaIPManagerDialog(project: Project) extends DialogWrapper(project) {
  init()
  setTitle(ScaledaBundle.message("windows.ip.manager.title"))
  setSize(800, 600)
  setResizable(true)

  var mainPanel: ScaledaIPManagerPanel = _

  override def createCenterPanel(): JComponent = {
    if (mainPanel == null) mainPanel = new ScaledaIPManagerPanel(project, setValid)
    mainPanel
  }

  private def setValid(ok: Boolean): Unit = setOKActionEnabled(ok)

  override def doOKAction(): Unit = {
    // Dispose editor (if has)
    if (mainPanel.editor != null) EditorFactory.getInstance.releaseEditor(mainPanel.editor)

    // Export IP information
    val ips               = mainPanel.toIPInstances
    implicit val scaledaProject: ScaledaProject = YmlRootManager.getInstance(project).getRoots.head.toScaledaProject
    val newProjectConfig = ProjectConfig.getConfig.get.copy(ips = ips)
    ProjectConfig.saveConfig(newProjectConfig, targetFile = new File(scaledaProject.configFile.get)) // TODO: can get?

    // Re-generate, only update caches from ProjectConfig layer, and before this `ips` in ProjectConfig should be updated
    KernelFileUtils.doUpdateStubCacheFromProject

    super.doOKAction()
  }

  override def doCancelAction(source: AWTEvent): Unit = {
    if (mainPanel.editor != null) EditorFactory.getInstance.releaseEditor(mainPanel.editor)
    super.doCancelAction(source)
  }

}

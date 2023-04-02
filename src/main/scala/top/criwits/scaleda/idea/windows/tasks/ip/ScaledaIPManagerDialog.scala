package top.criwits.scaleda
package idea.windows.tasks.ip

import idea.ScaledaBundle
import kernel.utils.KernelFileUtils

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

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
    if (mainPanel == null) mainPanel = new ScaledaIPManagerPanel(project)
    mainPanel
  }

  override def doOKAction(): Unit = {
    // Dispose editor (if has)
    if (mainPanel.editor != null) EditorFactory.getInstance.releaseEditor(mainPanel.editor)

    // Export IP information
    val ips = mainPanel.toIPInstances
    println(ips)

    // Re-generate, only update caches from ProjectConfig layer, and before this `ips` in ProjectConfig should be updated
    KernelFileUtils.doUpdateStubCacheFromProject()

    super.doOKAction()
  }

}

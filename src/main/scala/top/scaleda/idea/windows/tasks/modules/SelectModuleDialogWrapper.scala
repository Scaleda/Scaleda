package top.scaleda
package idea.windows.tasks.modules

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import idea.ScaledaBundle
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import javax.swing.JComponent

class SelectModuleDialogWrapper(project: Project) extends DialogWrapper(project) {
  var moduleTreePanel: ModuleTreePanel = _
  var selectedModule: ModuleDeclarationPsiNode = _

  init()
  setTitle(ScaledaBundle.message("windows.edit.module.select"))

  override def createCenterPanel(): JComponent = {
    if (moduleTreePanel == null)
      moduleTreePanel = new ModuleTreePanel(project, this)

    moduleTreePanel
  }

}

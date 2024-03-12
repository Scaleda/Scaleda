package top.scaleda
package idea.windows.tasks

import idea.ScaledaBundle
import idea.windows.tasks.modules.SelectModuleDialogWrapper
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project

class ScaledaSelectModuleAction(
    project: Project
) extends AnAction(
      ScaledaBundle.message("windows.edit.module.select"),
      ScaledaBundle.message("windows.edit.module.select"),
      null
    ) {

  var module: ModuleDeclarationPsiNode = _

  override def actionPerformed(e: AnActionEvent): Unit = {
    val wrapper = new SelectModuleDialogWrapper(project)
    wrapper.showAndGet()
    module = wrapper.selectedModule
  }
}

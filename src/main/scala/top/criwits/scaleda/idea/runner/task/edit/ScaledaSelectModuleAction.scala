package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import top.criwits.scaleda.idea.runner.task.edit.modules.SelectModuleDialogWrapper
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode

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

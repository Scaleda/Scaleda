package top.scaleda
package idea.windows.rightPanel.ip

import idea.ScaledaBundle

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import top.scaleda.idea.project.io.YmlRootManager

class ScaledaIPManageAction
    extends AnAction(
      ScaledaBundle.message("windows.ip.manager.title"),
      ScaledaBundle.message("windows.ip.manager.description"),
      AllIcons.Actions.Colors
    ) {
  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    val ymlRoots = YmlRootManager.getInstance(e.getProject).getRoots
    presentation.setEnabled(ymlRoots.nonEmpty)
  }
  override def actionPerformed(e: AnActionEvent): Unit = {
    val _r = new ScaledaIPManagerDialog(e.getProject).showAndGet()
  }
}

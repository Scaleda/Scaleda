package top.criwits.scaleda
package idea.utils

import kernel.project.ProjectManifest
import verilog.utils.ModuleUtils

import com.intellij.openapi.actionSystem._

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JTextField

class ChooseTopModuleListener(targetField: JTextField)(implicit manifest: ProjectManifest) extends ActionListener {
  override def actionPerformed(actionEvent: ActionEvent) = {
    val group   = new DefaultActionGroup()
    val modules = ModuleUtils.parseSourceSetTopModules(Set(manifest.project.getBasePath))
    if (modules.nonEmpty)
      modules.foreach(name =>
        group.add(new AnAction(name) {
          override def actionPerformed(e: AnActionEvent) = targetField.setText(name)
        })
      )
    else {
      // TODO: i18n
      group.add(new AnAction("No valid top module") {
        override def actionPerformed(e: AnActionEvent) = {}
      })
    }
    val popupMenu = ActionManager.getInstance().createActionPopupMenu(ActionPlaces.POPUP, group)
    popupMenu.getComponent.show(targetField, 0, targetField.getHeight)
  }
}

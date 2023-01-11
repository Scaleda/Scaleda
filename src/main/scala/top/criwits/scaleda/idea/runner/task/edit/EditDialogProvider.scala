package top.criwits.scaleda
package idea.runner.task.edit

import javax.swing.JPanel

trait EditDialogProvider {
  def getMainPanel: JPanel
}

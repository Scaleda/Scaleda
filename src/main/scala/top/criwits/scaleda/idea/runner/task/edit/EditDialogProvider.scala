package top.criwits.scaleda
package idea.runner.task.edit

import javax.swing.JPanel

trait EditDialogProvider[T] {
  def getMainPanel: JPanel
  def getData: T
}

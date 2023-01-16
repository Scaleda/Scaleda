package top.criwits.scaleda
package idea.runner.task.edit

import javax.swing.JPanel

trait EditDialog[T] {
  def getMainPanel: JPanel
  def getData: T
  def validateConfig: Boolean
}

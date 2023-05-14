package top.scaleda
package idea.windows.tasks

import javax.swing.JPanel

trait ScaledaEditPanelWrapper {
  def getPanel: JPanel
  def checkValue: Boolean
}

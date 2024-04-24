package top.scaleda
package idea.windows.rightPanel

import javax.swing.JPanel

trait ScaledaEditPanelWrapper {
  def getPanel: JPanel
  def checkValue: Boolean
}

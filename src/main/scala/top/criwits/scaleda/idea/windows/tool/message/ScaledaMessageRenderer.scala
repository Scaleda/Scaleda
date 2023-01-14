package top.criwits.scaleda
package idea.windows.tool.message

import java.awt.Component
import javax.swing.{JLabel, JList, ListCellRenderer}

class ScaledaMessageRenderer extends ListCellRenderer[ScaledaMessage] {
  override def getListCellRendererComponent(
      jList: JList[_ <: ScaledaMessage],
      e: ScaledaMessage,
      i: Int,
      b: Boolean,
      b1: Boolean
  ): Component = {
    new JLabel(e.text)
  }
}

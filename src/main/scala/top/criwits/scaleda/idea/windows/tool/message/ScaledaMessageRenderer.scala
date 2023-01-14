package top.criwits.scaleda
package idea.windows.tool.message

import com.intellij.icons.AllIcons
import top.criwits.scaleda.idea.utils.Icons

import java.awt.{Color, Component}
import javax.swing.{JLabel, JList, ListCellRenderer}

class ScaledaMessageRenderer extends ListCellRenderer[ScaledaMessage] {
  override def getListCellRendererComponent(
      jList: JList[_ <: ScaledaMessage],
      e: ScaledaMessage,
      i: Int,
      b: Boolean,
      b1: Boolean
  ): Component = {
    val label = new JLabel(e.text)
    import top.criwits.scaleda.kernel.utils.LogLevel._
    val icon = e.level match {
      case Debug => Icons.logVerbose
      case Info => AllIcons.General.Information
      case Warn => AllIcons.General.Warning
      case _ => AllIcons.General.Error
    }
    label.setIcon(icon)
    label
  }
}

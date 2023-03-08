package top.criwits.scaleda
package idea.windows.tool.message

import idea.utils.Icons

import com.intellij.icons.AllIcons
import com.intellij.ui.ColoredListCellRenderer

import javax.swing.JList

class ScaledaMessageRenderer extends ColoredListCellRenderer[ScaledaMessage] {
  // override def getListCellRendererComponent(
  //     list: JList[_ <: ScaledaMessage],
  //     value: ScaledaMessage,
  //     index: Int,
  //     selected: Boolean,
  //     hasFocus: Boolean
  // ): Component = {
  //   val label = new JLabel(value.text)
  //   import top.criwits.scaleda.kernel.utils.LogLevel._
  //   val icon = value.level match {
  //     case Debug => Icons.logVerbose
  //     case Info  => AllIcons.General.Information
  //     case Warn  => AllIcons.General.Warning
  //     case _     => AllIcons.General.Error
  //   }
  //   label.setIcon(icon)
  //   label
  // }

  override def customizeCellRenderer(
      list: JList[_ <: ScaledaMessage],
      value: ScaledaMessage,
      index: Int,
      selected: Boolean,
      hasFocus: Boolean
  ): Unit = {
    import top.criwits.scaleda.kernel.utils.LogLevel._
    val icon = value.level match {
      case Debug => Icons.logVerbose
      case Info  => AllIcons.General.Information
      case Warn  => AllIcons.General.Warning
      case _     => AllIcons.General.Error
    }
    setIcon(icon)
    append(value.text)
  }
}

package top.criwits.scaleda
package idea.windows.tool.message

import idea.utils.Icons

import com.intellij.icons.AllIcons
import com.intellij.ui.ColoredListCellRenderer

import javax.swing.JList
import scala.collection.mutable

class ScaledaMessageRenderer extends ColoredListCellRenderer[ScaledaMessage] {
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

object ScaledaMessageRendererImpl extends ScaledaMessageRenderer

object ScaledaMessageRenderer {
  private val allRenderers = new mutable.HashMap[String, ScaledaMessageRenderer]()

  def addRenderer(key: String, renderer: ScaledaMessageRenderer) = allRenderers.put(key, renderer)

  def getRenderer(key: String) = allRenderers.get(key)

  def removeRenderer(key: String) = allRenderers.remove(key)

  def getRendererMap = allRenderers
}

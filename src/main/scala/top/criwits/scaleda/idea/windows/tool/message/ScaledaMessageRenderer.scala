package top.criwits.scaleda
package idea.windows.tool.message

import idea.utils.Icons

import com.intellij.icons.AllIcons
import com.intellij.ui.{ColoredListCellRenderer, SimpleTextAttributes}

import javax.swing.JList
import scala.collection.mutable
import scala.util.matching.Regex

class ScaledaMessageRenderer extends ColoredListCellRenderer[ScaledaMessage] {
  protected val fileRegexString           = "([\\s\\w+:]*?/?(/.+/)?)((\\w+)\\.(\\w+))"
  protected val fileLineNumberRegexString = fileRegexString + "\\:(\\w+)"
  protected val fileRegex                 = new Regex(fileRegexString, "prefixPath", "filename")
  protected val fileLineNumberRegex       = new Regex(fileRegexString, "prefixPath", "filename", "line")

  protected def renderer(
      list: JList[_ <: ScaledaMessage],
      value: ScaledaMessage,
      index: Int,
      selected: Boolean,
      hasFocus: Boolean
  ): Seq[(String, Option[SimpleTextAttributes])] = {
    import top.criwits.scaleda.kernel.utils.LogLevel._
    val icon = value.level match {
      case Debug => Icons.logVerbose
      case Info  => AllIcons.General.Information
      case Warn  => AllIcons.General.Warning
      case _     => AllIcons.General.Error
    }
    setIcon(icon)
    Seq((value.text, None))
  }

  override final def customizeCellRenderer(
      list: JList[_ <: ScaledaMessage],
      value: ScaledaMessage,
      index: Int,
      selected: Boolean,
      hasFocus: Boolean
  ): Unit = {
    renderer(list, value, index, selected, hasFocus).foreach(d =>
      if (d._2.nonEmpty) append(d._1, d._2.get) else append(d._1)
    )
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

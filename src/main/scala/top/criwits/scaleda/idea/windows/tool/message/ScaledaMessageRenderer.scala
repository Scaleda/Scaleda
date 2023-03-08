package top.criwits.scaleda
package idea.windows.tool.message

import idea.utils.Icons

import com.intellij.icons.AllIcons
import com.intellij.ui.{ColoredListCellRenderer, SimpleTextAttributes}

import javax.swing.JList
import scala.collection.mutable
import scala.util.matching.Regex

class ScaledaMessageRenderer extends ColoredListCellRenderer[ScaledaMessage] {
  import ScaledaMessageRenderer._

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
    val fileMatches = fileRegex.findAllMatchIn(value.text).toSeq.sortBy(d => d.start - d.end)

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
  val fileRegexString = "((\\w+\\:)*?[^\\s'\"/\\\\\\?\\:]*?/?((/[^\\&]+/)|(\\\\[^\\&]+\\\\))?)((\\w+)\\.(\\w+))"
  val fileLineNumberRegexString = fileRegexString + "\\:(\\w+)"
  val fileRegex = new Regex(fileRegexString)
  val fileLineNumberRegex = new Regex(fileRegexString)

  private val allRenderers = new mutable.HashMap[String, ScaledaMessageRenderer]()

  def addRenderer(key: String, renderer: ScaledaMessageRenderer) = allRenderers.put(key, renderer)

  def getRenderer(key: String) = allRenderers.get(key)

  def removeRenderer(key: String) = allRenderers.remove(key)

  def getRendererMap = allRenderers
}

object TestFileMatches extends App {
  import ScaledaMessageRenderer._
  val text =
    "INFO: [Common 17-1381] The checkpoint '/home/chiro/programs/scaleda-sample-project/.synth/Vivado-Vivado Synth/waterfall/waterfall.runs/synth_1/waterfall.dcp' has been generated. && Run output will be captured here: /home/chiro/programs/scaleda-sample-project/.synth/Vivado-Vivado Synth/waterfall/waterfall.runs/synth_1/runme.log" +
      " C:\\Scaleda\\a.txt - caleda.vv"
  val fileMatches = fileRegex.findAllMatchIn(text).toSeq.sortBy(d => d.start - d.end)
  fileMatches.foreach(p => println("[" + p + "]"))
}

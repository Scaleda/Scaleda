package top.criwits.scaleda
package idea.windows.tool.message

import idea.utils.Icons

import com.intellij.icons.AllIcons
import com.intellij.ui.{ColoredListCellRenderer, SimpleTextAttributes}

import javax.swing.JList
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
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
    // val ranges          = ArrayBuffer[(Int, Int)]()
    // val matches         = fileOptionalLineNumberRegex.findAllMatchIn(value.text).toSeq.sortBy(d => d.start - d.end)
    // val selectedMatches = ArrayBuffer[Regex.Match]()
    // for (m <- matches) {
    //   if (
    //     !ranges.exists(i =>
    //       (i._1 <= m.start && m.start <= i._2) || (i._1 <= m.end && m.end <= i._2) || (i._1 <= m.start && m.end <= i._2)
    //     )
    //   ) {
    //     // if range not covered, select this match
    //     ranges.addOne((m.start, m.end))
    //     selectedMatches.addOne(m)
    //   }
    // }
    val results = ArrayBuffer[(String, Option[SimpleTextAttributes])]()
    val text    = value.text
    val matches = fileOptionalLineNumberRegex.findAllMatchIn(text).toSeq.sortBy(_.start)
    // val ranges  = matches.map(m => (m.start, m.end)).toSeq
    // KernelLogger.warn("ranges", ranges)
    // handle start
    matches.headOption.foreach(m => {
      val s = text.slice(0, m.start)
      if (s.nonEmpty) results.addOne((s, None))
    })
    for (i <- matches.indices) {
      // handle this match and next plain text
      results.addOne((matches(i).matched, Some(SimpleTextAttributes.LINK_ATTRIBUTES)))
      if (i < matches.size - 1) {
        if (matches(i).end < text.length - 1) {
          results.addOne((text.slice(matches(i).end + 1, matches(i + 1).start), None))
        }
      } else {
        results.addOne((text.slice(matches(i).end + 1, text.length), None))
      }
    }

    // Seq((value.text, None))
    results.toSeq
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
  val fileRegexString                   = "((\\w+\\:)*?[^\\s'\"/\\\\\\?\\:]*?/?((/[^\\&]+/)|(\\\\[^\\&]+\\\\))?)((\\w+)\\.(\\w+))"
  val fileLineNumberRegexString         = fileRegexString + "\\:(\\w+)"
  val fileOptionalLineNumberRegexString = fileRegexString + "(\\:(\\w+))?"
  val fileRegex                         = new Regex(fileRegexString)
  val fileLineNumberRegex               = new Regex(fileRegexString)
  val fileOptionalLineNumberRegex       = new Regex(fileOptionalLineNumberRegexString)

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
  println("slice:", "0123456789".slice(0, 1))
}

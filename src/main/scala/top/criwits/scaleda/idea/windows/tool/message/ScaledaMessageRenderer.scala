package top.criwits.scaleda
package idea.windows.tool.message

import idea.utils.Icons

import com.intellij.icons.AllIcons
import com.intellij.ui.{ColoredListCellRenderer, SimpleTextAttributes}

import java.util.regex.Pattern
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
    val text                     = (if (value.code.nonEmpty) s"[${value.code}] " else "") + value.text
    val matches                  = fileOptionalLineNumberRegex.findAllMatchIn(text).toSeq.sortBy(_.start)
    var filePath: Option[String] = None
    var fileLine: Option[Int]    = None
    if (matches.nonEmpty) {
      val results = ArrayBuffer[(String, Option[SimpleTextAttributes])]()
      // handle start
      matches.headOption.foreach(m => {
        val s = text.slice(0, m.start)
        if (s.nonEmpty) results.addOne((s, None))
      })
      for (i <- matches.indices) {
        // handle this match and next plain text
        results.addOne((matches(i).matched, Some(SimpleTextAttributes.LINK_ATTRIBUTES)))
        if (filePath.isEmpty) { // redo-match
          val m = fileOptionalLineNumberPattern.matcher(matches(i).matched)
          if (m.find()) {
            filePath = Some(m.group(1))
            fileLine = Option(m.group(m.groupCount())).map(n =>
              try Integer.parseInt(n)
              catch {
                case e: Throwable => 0
              }
            )
          }
        }
        if (i < matches.size - 1) {
          if (matches(i).end < text.length - 1) {
            results.addOne((text.slice(matches(i).end, matches(i + 1).start), None))
          }
        } else {
          results.addOne((text.slice(matches(i).end, text.length), None))
        }
      }
      // addFocusListener(new FocusListener {
      //   override def focusGained(focusEvent: FocusEvent) = {
      //     MainLogger.info("focusGained")
      //     filePath.foreach(path => {
      //       RpcService.pushGotoInfo(RpcService.RpcGotoInfo(path, fileLine.getOrElse(0), 0))
      //     })
      //   }
      //
      //   override def focusLost(focusEvent: FocusEvent) = {}
      // })
      results.toSeq
    } else {
      Seq((text, None))
    }
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

object ScaledaMessageRendererDefault extends ScaledaMessageRenderer

object ScaledaMessageRenderer {
  val fileRegexString                   = "(((\\w+:)*?[^\\s'\"/\\\\?:]*?/?((/[^&:]+/)|(\\\\[^&:]+\\\\))?)((\\w+)\\.(\\w+)))"
  val fileLineNumberRegexString         = fileRegexString + ":(\\w+)"
  val fileOptionalLineNumberRegexString = fileRegexString + "(:(\\w+))?"
  val fileRegex                         = new Regex(fileRegexString)
  val fileLineNumberRegex               = new Regex(fileLineNumberRegexString)
  val fileOptionalLineNumberRegex       = new Regex(fileOptionalLineNumberRegexString)
  val filePattern                       = Pattern.compile(fileRegexString)
  val fileLineNumberPattern             = Pattern.compile(fileLineNumberRegexString)
  val fileOptionalLineNumberPattern     = Pattern.compile(fileOptionalLineNumberRegexString)

  private val allRenderers = new mutable.HashMap[String, ScaledaMessageRenderer]()

  def addRenderer(key: String, renderer: ScaledaMessageRenderer) = allRenderers.put(key, renderer)

  def getRenderer(key: String) = allRenderers.get(key)

  def removeRenderer(key: String) = allRenderers.remove(key)

  def getRendererMap = allRenderers
}

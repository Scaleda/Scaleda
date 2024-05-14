package top.scaleda
package idea.toolchain

import idea.windows.bottomPanel.message._
import kernel.toolchain.impl.Quartus.internalID
import kernel.toolchain.runner.ScaledaRuntime
import kernel.utils.LogLevel

import java.util.regex.Pattern

object QuartusIdea extends ScaledaMessageToolchainParserProvider {
  object MessageParser extends ScaledaMessageToolchainParser {
    override def parse(rt: ScaledaRuntime, text: String, level: LogLevel.Value): Option[ScaledaMessage] = {
      val t        = text.trim
      val prepared = super.parse(rt, t, level)
      val p        = Pattern.compile("(Info|Warning|Error) ?(\\(\\d+\\))?: ?(.+)")
      val m        = p.matcher(t)
      if (m.find()) {
        // level was matched in super class
        val (tag, message) = (
          Option(m.group(2)).getOrElse("").replace("(", "").replace(")", ""),
          m.group(3)
        )
        prepared.map(p => {
          p.copy(code = tag, text = message)
        })
      } else {
        prepared
      }
    }
  }
  override def messageParser: ScaledaMessageToolchainParser = MessageParser

  private object MessageRenderer extends ScaledaMessageRenderer

  ScaledaMessageRenderer.addRenderer(internalID, MessageRenderer)
}

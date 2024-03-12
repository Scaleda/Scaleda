package top.scaleda
package idea.toolchain

import idea.windows.tool.message._
import kernel.toolchain.impl.Vivado.internalID
import kernel.utils.LogLevel
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime

import java.util.regex.Pattern

object VivadoIdea extends ScaledaMessageToolchainParserProvider {
  object MessageParser extends ScaledaMessageToolchainParser {
    override def parse(rt: ScaledaRuntime, text: String, level: LogLevel.Value): Option[ScaledaMessage] = {
      val prepared = super.parse(rt, text, level)
      val p        = Pattern.compile("(INFO|WARNING|ERROR): \\[(.+)] ?(.+)")
      val m        = p.matcher(text)
      if (m.find()) {
        // level was matched in super class
        val (tag, message) = (m.group(2), m.group(3))
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

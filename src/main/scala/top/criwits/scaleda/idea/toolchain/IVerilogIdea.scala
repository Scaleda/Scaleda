package top.criwits.scaleda
package idea.toolchain

import idea.windows.tool.message.{
  ScaledaMessage,
  ScaledaMessageRenderer,
  ScaledaMessageToolchainParser,
  ScaledaMessageToolchainParserProvider
}
import kernel.toolchain.impl.IVerilog.internalID
import kernel.utils.LogLevel

import org.jetbrains.annotations.Nls

object IVerilogIdea extends ScaledaMessageToolchainParserProvider {
  private object MessageParser extends ScaledaMessageToolchainParser {
    override def parse(source: String, @Nls text: String, level: LogLevel.Value): Option[ScaledaMessage] = {
      if (text.nonEmpty && level.id >= LogLevel.Info.id) {
        var messageLevel = LogLevel.Info
        if (text.toLowerCase.contains("error")) messageLevel = LogLevel.Error
        return Some(ScaledaMessage(text = text, level = messageLevel))
      }
      None
    }
  }
  private object MessageRenderer extends ScaledaMessageRenderer {}

  ScaledaMessageRenderer.addRenderer(internalID, MessageRenderer)

  override def messageParser: ScaledaMessageToolchainParser = MessageParser
}

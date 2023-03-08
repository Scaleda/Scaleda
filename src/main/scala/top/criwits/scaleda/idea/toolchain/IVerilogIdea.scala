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
      // TODO: implement iverilog parser here
      if (text.nonEmpty) {
        return Some(ScaledaMessage(text = text, level = LogLevel.Info))
      }
      None
    }
  }
  private object MessageRenderer extends ScaledaMessageRenderer {}

  ScaledaMessageRenderer.addRenderer(internalID, MessageRenderer)

  override def messageParser: ScaledaMessageToolchainParser = MessageParser
}

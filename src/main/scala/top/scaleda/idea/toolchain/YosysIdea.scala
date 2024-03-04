package top.scaleda
package idea.toolchain

import idea.windows.tool.message._
import kernel.toolchain.impl.IVerilog.internalID
import kernel.toolchain.runner.ScaledaRuntime
import kernel.utils.LogLevel

import org.jetbrains.annotations.Nls

object YosysIdea extends ScaledaMessageToolchainParserProvider {
  private object MessageParser extends ScaledaMessageToolchainParser {
    override def parse(rt: ScaledaRuntime, @Nls text: String, level: LogLevel.Value): Option[ScaledaMessage] = {
      if (text.nonEmpty && level.id >= LogLevel.Info.id) {
        super.parse(rt, text, level)
      } else None
    }
  }
  private object MessageRenderer extends ScaledaMessageRenderer

  ScaledaMessageRenderer.addRenderer(internalID, MessageRenderer)

  override def messageParser: ScaledaMessageToolchainParser = MessageParser
}

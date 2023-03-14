package top.criwits.scaleda
package idea.toolchain

import idea.runner.ScaledaRuntimeInfo
import idea.windows.tool.message._
import kernel.toolchain.impl.IVerilog.internalID
import kernel.utils.LogLevel

import org.jetbrains.annotations.Nls

object IVerilogIdea extends ScaledaMessageToolchainParserProvider {
  private object MessageParser extends ScaledaMessageToolchainDefaultParser {
    override def parse(rt: ScaledaRuntimeInfo, @Nls text: String, level: LogLevel.Value): Option[ScaledaMessage] = {
      if (text.nonEmpty && level.id >= LogLevel.Info.id) {
        super.parse(rt, text, level)
      } else None
    }
  }
  private object MessageRenderer extends ScaledaMessageRenderer

  ScaledaMessageRenderer.addRenderer(internalID, MessageRenderer)

  override def messageParser: ScaledaMessageToolchainParser = MessageParser
}

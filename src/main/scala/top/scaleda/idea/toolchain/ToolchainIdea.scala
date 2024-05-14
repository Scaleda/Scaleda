package top.scaleda
package idea.toolchain

import idea.windows.bottomPanel.message.ScaledaMessageToolchainParserProvider
import kernel.toolchain.impl.{IVerilog, Quartus, Vivado}

object ToolchainIdea {
  def toolchainMessageParser: Map[String, ScaledaMessageToolchainParserProvider] = Map(
    Vivado.internalID   -> VivadoIdea,
    Quartus.internalID  -> QuartusIdea,
    IVerilog.internalID -> IVerilogIdea
  )
}

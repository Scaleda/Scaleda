package top.scaleda
package idea.toolchain

import idea.windows.tool.message.ScaledaMessageToolchainParserProvider
import kernel.toolchain.impl.{IVerilog, Vivado}

object ToolchainIdea {
  def toolchainMessageParser: Map[String, ScaledaMessageToolchainParserProvider] = Map(
    Vivado.internalID   -> VivadoIdea,
    IVerilog.internalID -> IVerilogIdea
  )
}

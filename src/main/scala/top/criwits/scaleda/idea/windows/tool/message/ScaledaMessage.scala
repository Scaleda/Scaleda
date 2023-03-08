package top.criwits.scaleda
package idea.windows.tool.message

import kernel.utils.LogLevel

import top.criwits.scaleda.verilog.VerilogPSIFileRoot

case class ScaledaMessage(
    code: Int = 0,
    time: Long = 0,
    level: LogLevel.Value = LogLevel.Debug,
    text: String = "",
    file: Option[VerilogPSIFileRoot] = None,
    line: Option[Int] = None
)

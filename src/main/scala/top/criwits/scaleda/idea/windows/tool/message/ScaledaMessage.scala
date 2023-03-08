package top.criwits.scaleda
package idea.windows.tool.message

import kernel.utils.LogLevel
import verilog.VerilogPSIFileRoot

import org.jetbrains.annotations.Nls

case class ScaledaMessage(
    code: Int = 0,
    time: Long = 0,
    level: LogLevel.Value = LogLevel.Debug,
    @Nls text: String = "",
    file: Option[VerilogPSIFileRoot] = None,
    line: Option[Int] = None,
    toolchain: String = ""
)

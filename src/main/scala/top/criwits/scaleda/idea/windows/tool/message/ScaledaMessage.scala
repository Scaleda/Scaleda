package top.criwits.scaleda
package idea.windows.tool.message

import kernel.utils.LogLevel

case class ScaledaMessage(
    code: Int = 0,
    level: LogLevel.Value = LogLevel.Debug,
    text: String = ""
)

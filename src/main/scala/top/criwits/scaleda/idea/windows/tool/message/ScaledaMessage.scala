package top.criwits.scaleda
package idea.windows.tool.message

import kernel.utils.LogLevel

import org.jetbrains.annotations.Nls

import java.time.Instant

case class ScaledaMessage(
    // to identify message type, e.g. INFO: [/*Common 17-349*/] Got license for...
    code: String = "",
    time: Instant = Instant.now(),
    level: LogLevel.Value = LogLevel.Debug,
    @Nls text: String = "",
    file: Option[String] = None,
    line: Option[Int] = None,
    toolchain: String = ""
)

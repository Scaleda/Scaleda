package top.criwits.scaleda
package idea.windows.tool.message

import idea.windows.tool.logging.ScaledaLogReceiver
import kernel.toolchain.impl.Vivado
import kernel.utils.LogLevel

import java.util.regex.Pattern

class ScaledaMessageParser(
    handler: ScaledaMessage => Unit
) extends ScaledaLogReceiver {
  override def print(
      source: String,
      text: String,
      level: LogLevel.Value
  ): Unit = {
    if (source.contains(Vivado.internalID)) {
      // Vivado message
      val p = Pattern.compile("(INFO|WARNING|ERROR): \\[(.+)\\] ?(.+)")
      val m = p.matcher(text)
      if (m.find()) {
        val (levelText, tag, message) = (m.group(1), m.group(2), m.group(3))
        import LogLevel._
        val textedLevel = levelText match {
          case "INFO"    => Info
          case "WARNING" => Warn
          case _         => Error
        }
        handler(
          ScaledaMessage(
            text = s"[$tag] $message",
            level = textedLevel,
            time = System.currentTimeMillis()
          )
        )
      }
    }
  }
}

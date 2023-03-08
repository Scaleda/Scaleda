package top.criwits.scaleda
package idea.toolchain

import idea.windows.tool.message.{
  ScaledaMessage,
  ScaledaMessageRenderer,
  ScaledaMessageToolchainParser,
  ScaledaMessageToolchainParserProvider
}
import kernel.toolchain.impl.Vivado
import kernel.toolchain.impl.Vivado.internalID
import kernel.utils.LogLevel

import java.util.regex.Pattern

object VivadoIdea extends ScaledaMessageToolchainParserProvider {
  object MessageParser extends ScaledaMessageToolchainParser {
    override def parse(source: String, text: String, level: LogLevel.Value): Option[ScaledaMessage] = {
      if (source.contains(Vivado.internalID)) {
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
          Some(
            ScaledaMessage(
              text = s"[$tag] $message",
              level = textedLevel
            )
          )
        } else {
          None
        }
      } else {
        None
      }
    }
  }
  override def messageParser: ScaledaMessageToolchainParser = MessageParser

  private object MessageRenderer extends ScaledaMessageRenderer {}

  ScaledaMessageRenderer.addRenderer(internalID, MessageRenderer)
}

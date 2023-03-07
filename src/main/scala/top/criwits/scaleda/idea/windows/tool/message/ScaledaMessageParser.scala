package top.criwits.scaleda
package idea.windows.tool.message

import idea.windows.tool.logging.ScaledaLogReceiver
import kernel.utils.LogLevel

import scala.collection.mutable.ArrayBuffer

trait ScaledaMessageToolchainParser {
  def parse(source: String, text: String, level: LogLevel.Value): Option[ScaledaMessage]
}

object ScaledaMessageParser {
  private val allParsers = ArrayBuffer[ScaledaMessageToolchainParser]()
  def registerParser(parser: ScaledaMessageToolchainParser): Unit =
    allParsers.addOne(parser)
}

class ScaledaMessageParser(
    handler: ScaledaMessage => Unit
) extends ScaledaLogReceiver {
  override def print(
      source: String,
      text: String,
      level: LogLevel.Value
  ): Unit = {
    var parseDone = false
    for (parser <- ScaledaMessageParser.allParsers) {
      if (!parseDone) {
        val result = parser.parse(source, text, level)
        if (result.nonEmpty) {
          parseDone = true
          handler(result.get)
        }
      }
    }
  }
}

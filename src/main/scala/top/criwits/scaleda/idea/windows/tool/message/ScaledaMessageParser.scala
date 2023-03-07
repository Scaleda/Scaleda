package top.criwits.scaleda
package idea.windows.tool.message

import idea.windows.tool.logging.ScaledaLogReceiver
import kernel.utils.LogLevel

import scala.collection.mutable

trait ScaledaMessageToolchainParser {
  def parse(source: String, text: String, level: LogLevel.Value): Option[ScaledaMessage]
}

trait ScaledaMessageToolchainParserProvider {
  def parser: ScaledaMessageToolchainParser
}

object ScaledaMessageParser {
  private val allParsers = new mutable.HashMap[String, ScaledaMessageToolchainParser]()
  def registerParser(key: String, parser: ScaledaMessageToolchainParser): Unit =
    allParsers.put(key, parser)

  def removeParser(key: String) = allParsers.remove(key)
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
    for ((_k, parser) <- ScaledaMessageParser.allParsers) {
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

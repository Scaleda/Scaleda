package top.criwits.scaleda
package idea.windows.tool.message

import idea.runner.ScaledaRuntimeInfo
import idea.windows.tool.logging.ScaledaLogReceiver
import kernel.utils.LogLevel

import org.jetbrains.annotations.Nls

import scala.collection.mutable

trait ScaledaMessageToolchainParser {
  def parse(rt: ScaledaRuntimeInfo, @Nls text: String, level: LogLevel.Value): Option[ScaledaMessage]
}

trait ScaledaMessageToolchainParserProvider {
  def messageParser: ScaledaMessageToolchainParser
}

object ScaledaMessageParser {
  private val allParsers = new mutable.HashMap[String, ScaledaMessageToolchainParser]()

  def registerParser(key: String, parser: ScaledaMessageToolchainParser): Unit =
    allParsers.put(key, parser)

  def removeParser(key: String) = allParsers.remove(key)
}

/** Used to handle messages when running command
  * @param handler callback for logging service
  */
class ScaledaMessageParser(
    rt: ScaledaRuntimeInfo,
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
        val result = parser.parse(rt, text, level)
        if (result.nonEmpty) {
          parseDone = true
          handler(result.get)
        }
      }
    }
  }
}

package top.criwits.scaleda
package idea.windows.tool.message

import idea.runner.ScaledaRuntime
import idea.windows.tool.logging.ScaledaLogReceiver
import kernel.utils.LogLevel

import org.jetbrains.annotations.Nls

import java.util.regex.Pattern
import scala.collection.mutable

trait ScaledaMessageToolchainParser {
  def parse(rt: ScaledaRuntime, @Nls text: String, level: LogLevel.Value): Option[ScaledaMessage] = Some(
    defaultParse(rt, text, level)
  )

  protected def defaultParse(rt: ScaledaRuntime, @Nls text: String, level: LogLevel.Value): ScaledaMessage = {
    val levelPattern = Pattern.compile("\\b(info|warn|warning|error|fatal)\\b", Pattern.CASE_INSENSITIVE)
    val levelMatcher = levelPattern.matcher(text)
    // find first level
    val messageLevel = if (levelMatcher.find()) {
      levelMatcher.group().toLowerCase match {
        case "warn" | "warning" => LogLevel.Warn
        case "error"            => LogLevel.Error
        case "fatal"            => LogLevel.Fatal
        case "info" | _         => LogLevel.Info
      }
    } else LogLevel.Info
    val filePattern = ScaledaMessageRenderer.fileOptionalLineNumberPattern
    val fileMatcher = filePattern.matcher(text)
    // find first
    val (filePath, fileLine) = if (fileMatcher.find()) {
      // not found, returns null
      val path = fileMatcher.group(1)
      val line: Option[Int] =
        try {
          Some(Integer.parseInt(fileMatcher.group(fileMatcher.groupCount())))
        } catch {
          case e: NumberFormatException => None
        }
      (Option(path), line)
    } else (None, None)
    ScaledaMessage(text = text, level = messageLevel, line = fileLine, file = filePath)
  }
}

trait ScaledaMessageToolchainParserProvider {
  def messageParser: ScaledaMessageToolchainParser
}

object ScaledaMessageToolchainDefaultParserImpl extends ScaledaMessageToolchainParser

object ScaledaMessageParser {
  private val allParsers = new mutable.HashMap[String, ScaledaMessageToolchainParser]()

  def registerParser(key: String, parser: ScaledaMessageToolchainParser): Unit =
    allParsers.put(key, parser)

  def removeParser(key: String) = allParsers.remove(key)
}

/** Used to handle messages when running command
  *
  * @param handler callback for logging service
  */
class ScaledaMessageParser(
    rt: ScaledaRuntime,
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

object ScaledaMessageToolchainParserDefaultProvider extends ScaledaMessageToolchainParserProvider {
  override def messageParser = ScaledaMessageToolchainDefaultParserImpl
}

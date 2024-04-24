package top.scaleda
package idea.windows.bottomPanel.message

import idea.windows.bottomPanel.console.ConsoleReceiver
import kernel.utils.{FileReplaceContext, LogLevel}

import org.jetbrains.annotations.Nls
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime

import java.util.regex.Pattern
import scala.collection.mutable

trait ScaledaMessageToolchainParser {
  def parse(rt: ScaledaRuntime, text: String, level: LogLevel.Value): Option[ScaledaMessage] = Some(
    defaultParse(rt, text, level)
  )

  protected def defaultParse(rt: ScaledaRuntime, text: String, level: LogLevel.Value): ScaledaMessage = {
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
    var textNow     = text
    var filePathUse = filePath
    var fileLineUse = fileLine
    rt.context
      .get("replaceFiles")
      .map(_.asInstanceOf[Seq[FileReplaceContext]])
      .foreach(_.foreach(replace => {
        textNow = textNow.replace(replace.to.getAbsolutePath, replace.from.getAbsolutePath)
        if (filePathUse.nonEmpty && filePathUse.get == replace.to.getAbsolutePath) {
          filePathUse = Some(replace.from.getAbsolutePath)
          if (fileLineUse.nonEmpty)
            fileLineUse = fileLineUse.map(n => if (n > replace.lineOffsetStart) n - replace.lineOffset else n)
        }
      }))
    ScaledaMessage(text = textNow, level = messageLevel, line = fileLineUse, file = filePathUse)
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

object ScaledaMessageToolchainParserDefaultProvider extends ScaledaMessageToolchainParserProvider {
  override def messageParser = ScaledaMessageToolchainDefaultParserImpl
}

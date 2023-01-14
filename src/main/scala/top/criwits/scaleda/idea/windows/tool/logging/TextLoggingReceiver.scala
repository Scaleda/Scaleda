package top.criwits.scaleda
package idea.windows.tool.logging

import kernel.utils.LogLevel

import com.intellij.execution.ui.ConsoleView
import top.criwits.scaleda.idea.utils.MainLogger

import scala.language.implicitConversions

trait TextLoggingReceiver {
  def print(text: String, level: LogLevel.Value): Unit
}

object TextLoggingReceiver {
  implicit def apply(consoleView: ConsoleView): TextLoggingReceiver =
    (text: String, level: LogLevel.Value) => consoleView.print(text, MainLogger.consoleLevel(level))
}
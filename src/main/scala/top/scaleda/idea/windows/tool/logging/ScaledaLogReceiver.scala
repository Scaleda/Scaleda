package top.scaleda
package idea.windows.tool.logging

import kernel.utils.LogLevel

import com.intellij.execution.ui.ConsoleView
import top.scaleda.idea.utils.MainLogger

import scala.language.implicitConversions

trait ScaledaLogReceiver {
  def print(source: String, text: String, level: LogLevel.Value): Unit
}

object ScaledaLogReceiver {
  implicit def apply(consoleView: ConsoleView): ScaledaLogReceiver =
    (_: String, text: String, level: LogLevel.Value) => consoleView.print(text, MainLogger.consoleLevel(level))
}
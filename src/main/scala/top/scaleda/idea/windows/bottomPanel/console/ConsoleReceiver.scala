package top.scaleda
package idea.windows.bottomPanel.console

import kernel.utils.LogLevel

import com.intellij.execution.ui.ConsoleView
import top.scaleda.idea.utils.ScaledaIdeaLogger

import scala.language.implicitConversions

/**
 * A [[ConsoleReceiver]] is registered in [[ConsoleService]], fired when a console output comes.
 */
trait ConsoleReceiver {
  /**
   * Called when message comes.
   * @param text the message
   * @param level message level
   */
  def print(s: String, text: String, level: LogLevel.Value): Unit

  /**
   * Clear this receiver. For console it means clear the console, while for message list it means clear the list.
   */
  def clear: Unit = {}
}

object ConsoleReceiver {
  /**
   * This implicit conversion is used to make [[ConsoleView]] seems to inherit [[ConsoleReceiver]].
   * @param consoleView A [[ConsoleView]] instance, which is used to print messages.
   * @return
   */
  implicit def apply(consoleView: ConsoleView): ConsoleReceiver =
    (_: String, text: String, level: LogLevel.Value) => consoleView.print(text, ConsoleLogger.consoleLevel(level))
}
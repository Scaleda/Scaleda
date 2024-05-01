package top.scaleda
package idea.windows.bottomPanel.console

import kernel.utils.LogLevel

import top.scaleda.idea.windows.bottomPanel.ConsoleService

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
  def print(text: String, level: LogLevel.Value): Unit

  /**
   * Clear this receiver. For console it means clear the console, while for message list it means clear the list.
   */
  def clear(): Unit = {}

  /**
   * Get the display name of this receiver.
   * @return
   */
  def getDisplayName: String
}
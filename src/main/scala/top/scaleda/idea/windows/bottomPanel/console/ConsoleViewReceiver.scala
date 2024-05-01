package top.scaleda
package idea.windows.bottomPanel.console

import kernel.utils.LogLevel
import kernel.utils.LogLevel._

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}

class ConsoleViewReceiver(consoleView: ConsoleView, displayName: String) extends ConsoleReceiver {

  /** Called when message comes.
    *
    * @param text  the message
    * @param level message level
    */
  override def print(text: String, level: LogLevel.Value): Unit = consoleView.print(
    text.strip() + "\n",
    level match {
      case Debug   => ConsoleViewContentType.LOG_DEBUG_OUTPUT
      case Verbose => ConsoleViewContentType.LOG_VERBOSE_OUTPUT
      case Info    => ConsoleViewContentType.LOG_INFO_OUTPUT
      case Warn    => ConsoleViewContentType.LOG_WARNING_OUTPUT
      case _       => ConsoleViewContentType.LOG_ERROR_OUTPUT
    }
  )

  /** Clear this receiver. For console it means clear the console, while for message list it means clear the list.
    */
  override def clear(): Unit = consoleView.clear()

  override def getDisplayName: String = displayName
}

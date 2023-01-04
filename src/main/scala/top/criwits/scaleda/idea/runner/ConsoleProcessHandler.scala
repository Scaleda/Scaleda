package top.criwits.scaleda
package idea.runner

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.{KillableColoredProcessHandler, ProcessTerminatedListener}
import com.intellij.execution.process.mediator.rpc.CommandLine
import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import com.intellij.openapi.util.Key

import java.io.OutputStreamWriter

class ConsoleProcessHandler(
                           private val consoleView: ConsoleView,
                           commandLine: GeneralCommandLine,
                           showExitCode: Boolean = true
                           ) extends KillableColoredProcessHandler(commandLine) {
  var outputContent = ""
  if (showExitCode) {
    ProcessTerminatedListener.attach(this)
  }

  private def append(text: String, attributes: Key[_]): Unit = {
    consoleView.print(text, ConsoleViewContentType.getConsoleViewType(attributes))
    outputContent += text
  }

  override def coloredTextAvailable(text: String, attributes: Key[_]): Unit = {
    append(text, attributes)
  }

  private val processInputWriter = new OutputStreamWriter(this.getProcessInput)
  def input(text: String): Unit = processInputWriter.append(text)
  def flush(): Unit = processInputWriter.flush()
}

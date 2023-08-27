package top.scaleda
package idea.windows.bottomPanel.console

import idea.windows.bottomPanel.ConsoleService
import kernel.utils.LogLevel
import kernel.utils.LogLevel._

import com.intellij.execution.actions.ClearConsoleAction
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionManager, ActionToolbar, DefaultActionGroup}
import com.intellij.openapi.editor.actions.{ScrollToTheEndToolbarAction, ToggleUseSoftWrapsToolbarAction}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer

import scala.jdk.javaapi.CollectionConverters

class ConsolePanel(
    parent: Disposable,
    project: Project
) extends SimpleToolWindowPanel(false, true) {
  // create a new console
  private val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  builder.setViewer(true)
  private val consoleView = builder.getConsole

  // register to console service
  val receiver: ConsoleReceiver = new ConsoleReceiver {
    override def print(text: String, level: LogLevel.Value): Unit = consoleView.print(
      text,
      level match {
        case Debug   => ConsoleViewContentType.LOG_DEBUG_OUTPUT
        case Verbose => ConsoleViewContentType.LOG_VERBOSE_OUTPUT
        case Info    => ConsoleViewContentType.LOG_INFO_OUTPUT
        case Warn    => ConsoleViewContentType.LOG_WARNING_OUTPUT
        case _       => ConsoleViewContentType.LOG_ERROR_OUTPUT
      }
    )
    override def clear(): Unit = consoleView.clear()
  }
  private val service = project.getService(classOf[ConsoleService])
  service.addListener(receiver)

  // dispose console when parent is disposed
  Disposer.register(parent, consoleView)

  val group = new DefaultActionGroup()
  consoleView.getComponent
  group.addAll(
    CollectionConverters.asJava(
      consoleView
        .createConsoleActions()
        .filter(it =>
          it.isInstanceOf[ClearConsoleAction] || it
            .isInstanceOf[ScrollToTheEndToolbarAction] || it
            .isInstanceOf[ToggleUseSoftWrapsToolbarAction]
        )
    )
  )

  val toolbar: ActionToolbar = {
    ActionManager
      .getInstance()
      .createActionToolbar(
        "Scaleda Toolbar", // ?
        group,
        false
      )
  }

  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)
  setContent(consoleView.getComponent)
}

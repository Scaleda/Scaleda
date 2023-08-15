package top.scaleda
package idea.windows.bottomPanel

import com.intellij.execution.actions.ClearConsoleAction
import com.intellij.execution.filters.{TextConsoleBuilder, TextConsoleBuilderFactory}
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionManager, ActionToolbar, DefaultActionGroup}
import com.intellij.openapi.editor.actions.{ScrollToTheEndToolbarAction, ToggleUseSoftWrapsToolbarAction}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer
import top.scaleda.idea.windows.bottomPanel.console.ConsoleService

import scala.jdk.javaapi.CollectionConverters

class ScaledaConsolePanel(
                           parent: Disposable,
                           project: Project,
                           consoleID: String
) extends SimpleToolWindowPanel(false, true) {
  // create a new console
  private val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  builder.setViewer(true)
  private val consoleView = builder.getConsole

  // register to console service
  private val service = project.getService(classOf[ConsoleService])
  service.addListener(consoleID, consoleView)


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
        "Scaleda Toolbar",
        group,
        false
      )
  }

  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)
  setContent(consoleView.getComponent)
}

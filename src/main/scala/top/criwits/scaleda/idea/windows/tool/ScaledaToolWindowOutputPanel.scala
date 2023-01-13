package top.criwits.scaleda
package idea.windows.tool

import idea.windows.tool.logging.ScaledaConsoleService

import com.intellij.execution.actions.ClearConsoleAction
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionManager, ActionToolbar, DefaultActionGroup}
import com.intellij.openapi.editor.actions.{ScrollToTheEndToolbarAction, ToggleUseSoftWrapsToolbarAction}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer

import scala.jdk.javaapi.CollectionConverters

class ScaledaToolWindowOutputPanel(
    parent: Disposable,
    project: Project,
    logSourceId: String
) extends SimpleToolWindowPanel(false, true) {
  val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  builder.setViewer(true)
  private val consoleView = builder.getConsole
  val service = project.getService(classOf[ScaledaConsoleService])
  service.addListener(logSourceId, consoleView)
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

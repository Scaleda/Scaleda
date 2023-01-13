package top.criwits.scaleda
package idea.windows.tool

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.actionSystem.{ActionManager, ActionToolbar, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel

class ScaledaToolWindowOutputPanel(project: Project) extends SimpleToolWindowPanel(false, true) {
  // the toolbar
  val toolbar: ActionToolbar = {
    ActionManager.getInstance().createActionToolbar(
      "Scaleda Toolbar",
      new DefaultActionGroup(), // FIXME
      false
    )
  }

  // the console view
  // val consoleView: ConsoleView = {
  //   val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  //   builder.setViewer(true)
  //   builder.getConsole
  // }

  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)
  // setContent(consoleView.getComponent)

  def showPanel(): Unit = {
    val contentManager = ScaledaToolWindowFactory.toolWindow(project).getContentManager
    contentManager.setSelectedContent(contentManager.getContent(0))
  }

}

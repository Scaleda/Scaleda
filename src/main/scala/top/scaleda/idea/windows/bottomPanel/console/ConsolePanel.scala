package top.scaleda
package idea.windows.bottomPanel.console

import idea.utils.ScaledaIdeaLogger
import idea.windows.bottomPanel.ConsoleService

import com.intellij.execution.actions.ClearConsoleAction
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.actionSystem.{ActionManager, ActionToolbar, DefaultActionGroup}
import com.intellij.openapi.editor.actions.{ScrollToTheEndToolbarAction, ToggleUseSoftWrapsToolbarAction}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer

import scala.jdk.javaapi.CollectionConverters

class ConsolePanel(
    consoleTab: ConsoleTab,
    project: Project,
    id: String,
    displayName: String
) extends SimpleToolWindowPanel(false, true) {
  // create a new console
  private val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  builder.setViewer(true)
  private val consoleView = builder.getConsole

  def getConsoleTab: ConsoleTab = consoleTab

  // register to console service
  val receiver: ConsoleReceiver = new ConsoleViewReceiver(consoleView, displayName)
  private val service           = project.getService(classOf[ConsoleService])

  def registerConsoleReceiver(attach: Boolean): Unit = {
    if (!attach) {
      val runningId = service.queryRunningIdByDisplayName(displayName).filter(_ != id)
      // foreach { activationId: String =>
      //   {
      //     case Some(activationId) =>
      //       ScaledaIdeaLogger.debug(s"still running id $activationId, create new console tab")
      //     // service.removeListenerByKey(activationId)
      //     // getConsoleTab.getParent.removeConsoleTab(activationId)
      //     case None =>
      if (runningId.isEmpty) {
        getConsoleTab.getParent.findConsoleTabByName(displayName) foreach { tab =>
          if (!tab.getParent.removeConsoleTab(tab)) {
            ScaledaIdeaLogger.debug(s"failed to remove console tab: $tab")
          }
        }
      } else {
        ScaledaIdeaLogger.debug(s"still running id ${runningId.mkString(", ")}, create new console tab")
      }
      //   }
      // }
    }
    service.addListener(receiver, id)
  }

  // registerConsoleReceiver(false)

  // dispose console when parent is disposed
  Disposer.register(consoleTab, consoleView)

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

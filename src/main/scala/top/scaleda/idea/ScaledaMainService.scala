package top.scaleda
package idea

import idea.utils.MainLogger
import kernel.template.Template
import kernel.utils.KernelLogger
import idea.utils.Icons

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}
import top.scaleda.idea.windows.tasks.ScaledaRunWindowFactory
import top.scaleda.idea.windows.tool.ScaledaToolWindowFactory

/**
 * Scaleda Main Service for Project
 */
//noinspection ApiStatus
final class ScaledaMainService(val project: Project) extends Disposable {
  // copy kernel logs to main logger
  KernelLogger.append(MainLogger)
  // init jinjia
  Template.initJinja()

  def initToolWindows(): Unit = {
    val toolWindowManager = ToolWindowManager.getInstance(project)

    toolWindowManager.invokeLater(() => {
      val builder =
        new RegisterToolWindowTaskBuilder(ScaledaRunWindowFactory.WINDOW_ID)
      builder.icon = Icons.mainSmall
      builder.contentFactory = new ScaledaRunWindowFactory
      builder.anchor = ToolWindowAnchor.RIGHT
      // builder.stripeTitle = ScaledaBundle.message("tasks.tool.window.title")

      // FIXME: Internal API
      val toolWindow = toolWindowManager.registerToolWindow(builder.build())
    })

    toolWindowManager.invokeLater(() => {
      val builder =
        new RegisterToolWindowTaskBuilder(ScaledaToolWindowFactory.WINDOW_ID)
      builder.icon = Icons.mainSmall
      builder.contentFactory = new ScaledaToolWindowFactory
      builder.anchor = ToolWindowAnchor.BOTTOM
      builder.shouldBeAvailable = true

      val messageWindow = toolWindowManager.registerToolWindow(builder.build())
    })
  }

  override def dispose(): Unit = {
    Template.destroyJinja()
    KernelLogger.clearOthers()
  }

}

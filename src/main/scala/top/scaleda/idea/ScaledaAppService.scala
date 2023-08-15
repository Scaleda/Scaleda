package top.scaleda
package idea

import idea.utils.ScaledaIdeaLogger
import kernel.template.Template
import kernel.utils.KernelLogger
import idea.utils.Icons

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}
import top.scaleda.idea.windows.rightPanel.ScaledaRunWindowFactory
import top.scaleda.idea.windows.bottomPanel.ScaledaMessagesWindowFactory

trait ScaledaAppService extends Disposable {
  def initLoggers(): Unit
  def initJinja(): Unit
}

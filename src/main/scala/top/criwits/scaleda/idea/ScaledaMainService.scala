package top.criwits.scaleda
package idea

import idea.utils.{MainLogger, Notification, OutputLogger}
import kernel.template.Template
import kernel.utils.KernelLogger

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.ProjectManager
import top.criwits.scaleda.idea.windows.tool.ScaledaToolWindowFactory

final class ScaledaMainService extends Disposable {
  // override def dispose() = disposer.dispose()
  override def dispose() = {
    // disposer.dispose()
    // MainLogger.dispose()
    MainLogger.consoleView.foreach(c => c.dispose())
    // ProjectManager.getInstance().getOpenProjects.foreach(p => {
    //   ScaledaToolWindowFactory.outputPanel(p).consoleView.dispose()
    //   ScaledaToolWindowFactory.logPanel(p).consoleView.dispose()
    // })
    Template.destroyJinja()
    KernelLogger.clearOthers()
  }
}

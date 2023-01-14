package top.criwits.scaleda
package idea.windows.tool.message

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import top.criwits.scaleda.idea.windows.tool.logging.ScaledaLoggingService

class ScaledaMessageTab(project: Project, msgSourceId: String)
    extends Disposable {
  override def dispose() = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.stop(msgSourceId)
  }
}

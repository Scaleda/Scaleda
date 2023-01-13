package top.criwits.scaleda
package idea.windows.tool.logging

import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.openapi.project.Project
import top.criwits.scaleda.idea.windows.tool.ScaledaDataKeys

class LogConsoleViewImpl(project: Project, sourceId: String)
  extends ConsoleViewImpl(project, true) {
  override def getData(dataId: String): AnyRef =
    if (ScaledaDataKeys.LOG_SOURCE_ID.is(dataId)) sourceId
    else super.getData(dataId)
}

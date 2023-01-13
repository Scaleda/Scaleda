package top.criwits.scaleda
package idea.windows.tool.logging

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

class ConsoleTab(project: Project, logSourceId: String) extends Disposable{
  val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  builder.setViewer(true)
  private val consoleView = builder.getConsole
  val service = project.getService(classOf[ScaledaLoggingService])
  service.addListener(logSourceId, consoleView)
  Disposer.register(this, consoleView)

  def getContent = consoleView.getComponent

  override def dispose() = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.stop(logSourceId)
  }
}

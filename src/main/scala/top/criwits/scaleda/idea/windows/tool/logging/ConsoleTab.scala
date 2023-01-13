package top.criwits.scaleda
package idea.windows.tool.logging

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

import java.awt.BorderLayout
import javax.swing.JPanel

class ConsoleTab(project: Project, logSourceId: String) extends Disposable{
  // private val consoleView = new LogConsoleViewImpl(project, logSourceId)
  val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  builder.setViewer(true)
  private val consoleView = builder.getConsole
  // private val console = new JPanel()
  // val content = new JPanel()

  // console.add(consoleView.getComponent, BorderLayout.CENTER)
  // content.add(consoleView.getComponent, BorderLayout.CENTER)
  // content.add(console, BorderLayout.CENTER)
  Disposer.register(this, consoleView)

  def getContent = consoleView.getComponent

  override def dispose() = {
    val service = project.getService(classOf[ScaledaLoggingService])
    service.stop(logSourceId)
    // content.removeAll()
  }
}

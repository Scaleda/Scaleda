package top.criwits.scaleda
package idea.windows.tool.logging

import idea.windows.tool.ScaledaDataKeys

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.{Disposer, Key}
import com.intellij.ui.content.{
  ContentFactory,
  ContentManager,
  ContentManagerListener
}

import javax.swing.JPanel

class ConsoleTabManager(project: Project, contentManager: ContentManager)
    extends ContentManagerListener
    with Disposable {
  private val LOG_SOURCE_KEY =
    Key.create[String](ScaledaDataKeys.LOG_SOURCE_ID.getName)

  val logService = project.getService(classOf[ScaledaLoggingService])
  val contentFactory = ContentFactory.SERVICE.getInstance()
  private val emptyContent = contentFactory.createContent(
    new EmptyTabContentView(project).getContent,
    "",
    false
  )

  contentManager.addContentManagerListener(this)

  contentManager.addContent(emptyContent)

  def addTab(logSourceId: String, name: String, switchTo: Boolean = true) = {
    // remove empty content
    if (contentManager.getContentCount == 1)
      contentManager.removeContent(emptyContent, false)
    val consoleTab = new ConsoleTab(project, logSourceId)
    Disposer.register(this, consoleTab)

    // what's this?
    // content.putUserData(LOG_SOURCE_KEY, logSourceId)
    addPanel(consoleTab.getPanel, name, switchTo = switchTo)
  }

  def addPanel(panel: JPanel, name: String, switchTo: Boolean = true) = {
    val content = contentFactory.createContent(panel, name, false)
    contentManager.addContent(content)
    if (switchTo) contentManager.setSelectedContent(content)
  }

  override def dispose(): Unit = {}
}

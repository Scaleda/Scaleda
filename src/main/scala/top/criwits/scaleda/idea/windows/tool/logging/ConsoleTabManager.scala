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

class ConsoleTabManager(project: Project, contentManager: ContentManager)
    extends ContentManagerListener
    with Disposable
    // with LogSourceListener
    {
  private val LOG_SOURCE_KEY =
    Key.create[String](ScaledaDataKeys.LOG_SOURCE_ID.getName)

  val logService = project.getService(classOf[ScaledaLoggingService])
  val contentFactory = ContentFactory.SERVICE.getInstance()
  val emptyContent = contentFactory.createContent(
    new NoActionTabsView(project).getContent,
    "",
    false
  )

  contentManager.addContentManagerListener(this)

  contentManager.addContent(emptyContent)

  def addTab(logSourceId: String, name: String) = {
    // remove empty content
    if (contentManager.getContentCount == 1)
      contentManager.removeContent(emptyContent, false)
    val consoleTab = new ConsoleTab(project, logSourceId)
    Disposer.register(this, consoleTab)
    val content = contentFactory.createContent(consoleTab.getContent, name, false)
    content.putUserData(LOG_SOURCE_KEY, logSourceId)
    contentManager.addContent(content)
    contentManager.setSelectedContent(content)
  }

  override def dispose(): Unit = {}

  // override def newContentAdded(sourceId: String): Unit = ???
  //
  // override def sourceStarted(sourceId: String): Boolean = ???
  //
  // override def sourceStopped(sourceId: String): Boolean = ???
}

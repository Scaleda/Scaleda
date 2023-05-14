package top.scaleda
package idea.windows.tool.logging

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.content.{ContentFactory, ContentManager, ContentManagerListener}
import org.jetbrains.annotations.Nls

import javax.swing.JPanel

class ConsoleTabManager(project: Project, contentManager: ContentManager)
    extends ContentManagerListener
    with Disposable {

  val contentFactory = ContentFactory.getInstance()
  // if (ideaVersion.toIntOption.exists(_ >= 230)) ContentFactory.getInstance()
  // else ContentFactory.SERVICE.getInstance()
  private val emptyContent = contentFactory.createContent(
    new EmptyTabContentView().getContent,
    "",
    false
  )

  contentManager.addContentManagerListener(this)

  contentManager.addContent(emptyContent)

  def addTab(logSourceId: String, @Nls name: String, switchTo: Boolean = true) = {
    // remove empty content
    if (contentManager.getContentCount == 1)
      contentManager.removeContent(emptyContent, false)
    val consoleTab = new ConsoleTab(project, logSourceId)
    Disposer.register(this, consoleTab)
    addPanel(consoleTab.getPanel, name, switchTo = switchTo)
  }

  def addPanel(panel: JPanel, @Nls name: String, switchTo: Boolean = true) = {
    val content = contentFactory.createContent(panel, name, false)
    content.setPreferredFocusableComponent(panel)
    contentManager.addContent(content)
    if (switchTo) contentManager.setSelectedContent(content)
  }

  def setSelectedContent(name: String): Boolean = {
    val found = contentManager.findContent(name)
    if (found != null) {
      contentManager.setSelectedContent(found, true)
      true
    } else {
      false
    }
  }

  override def dispose(): Unit = {}
}

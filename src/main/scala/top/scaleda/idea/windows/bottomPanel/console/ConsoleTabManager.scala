package top.scaleda
package idea.windows.bottomPanel.console

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.content.{ContentFactory, ContentManager, ContentManagerListener}
import org.jetbrains.annotations.Nls

import javax.swing.JPanel

class ConsoleTabManager(project: Project, contentManager: ContentManager)
    extends ContentManagerListener
    with Disposable {

  private val contentFactory: ContentFactory = ContentFactory.getInstance()

  //noinspection ScalaExtractStringToBundle,ScalaUnusedExpression
  // private val contentStub = contentFactory.createContent(
  //   new EmptyTabContentView().getContent,
  //   "Stub",
  //   false
  // )

  contentManager.addContentManagerListener(this)
  // contentManager.addContent(contentStub)

  /**
   * Add a console tab to the bottom panel
   * Should be called only once!
   * @param name
   * @param switchTo
   */
  def addConsoleTab(@Nls name: String, switchTo: Boolean = true): Unit = {
    // remove stub if exists
    // if (contentManager.getContentCount == 1)
    //   contentManager.removeContent(contentStub, false)
    val consoleTab = new ConsoleTab(project)
    Disposer.register(this, consoleTab)
    addPanel(consoleTab.getPanel, name, switchTo = switchTo)
  }

  def addPanel(panel: JPanel, @Nls name: String, switchTo: Boolean = true): Unit = {
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

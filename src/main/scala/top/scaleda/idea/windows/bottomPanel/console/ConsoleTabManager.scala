package top.scaleda
package idea.windows.bottomPanel.console

import idea.utils.ScaledaIdeaLogger

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.content.{ContentFactory, ContentManager, ContentManagerListener}

import javax.swing.JPanel

class ConsoleTabManager(project: Project, contentManager: ContentManager)
    extends ContentManagerListener
    with Disposable {

  private val contentFactory: ContentFactory = ContentFactory.getInstance()

  contentManager.addContentManagerListener(this)

  def addConsoleTab(id: String, displayName: String, switchTo: Boolean = true, closeable: Boolean = true): Unit = {
    getConsoleTabById(id) match {
      case Some(consoleTab) =>
        ScaledaIdeaLogger.debug(s"attach to the existing console tab, key: $id")
        consoleTab.getPanel.registerConsoleReceiver(true)
        if (switchTo) setSelectedContent(consoleTab.getDisplayName)
      case None =>
        ScaledaIdeaLogger.debug(s"create a new console tab for key: $id")
        val consoleTab = new ConsoleTab(this, project, id, displayName)
        addPanel(consoleTab.getPanel, displayName, switchTo = switchTo, closeable = closeable)
    }
  }

  def addPanel(
      panel: JPanel,
      displayName: String,
      switchTo: Boolean = true,
      closeable: Boolean = true
  ): Unit = {
    val content = contentFactory.createContent(panel, displayName, false)
    content.setPreferredFocusableComponent(panel)
    content.setCloseable(closeable)
    contentManager.addContent(content)
    if (switchTo) contentManager.setSelectedContent(content)
  }

  def removeConsoleTabById(id: String): Unit = {
    getConsoleTabById(id) match {
      case Some(consoleTab) =>
        contentManager.removeContent(contentManager.getContents.find(_.getComponent == consoleTab.getPanel).get, true)
      case None =>
    }
  }

  def removeConsoleTab(consoleTab: ConsoleTab): Boolean = {
    contentManager.getContents.find(_.getPreferredFocusableComponent == consoleTab.getPanel) match {
      case None => false
      case Some(found) =>
        contentManager.removeContent(found, true)
        true
    }
  }

  def getConsoleTabById(id: String): Option[ConsoleTab] = {
    contentManager.getContents
      .filter(_.getPreferredFocusableComponent.isInstanceOf[ConsolePanel])
      .map(_.getPreferredFocusableComponent.asInstanceOf[ConsolePanel].getConsoleTab)
      .find(_.getMessageId == id)
  }

  def findConsoleTabByName(name: String): Seq[ConsoleTab] = {
    contentManager.getContents
      .filter(_.getPreferredFocusableComponent.isInstanceOf[ConsolePanel])
      .map(_.getPreferredFocusableComponent.asInstanceOf[ConsolePanel])
      .filter(_.getConsoleTab.getDisplayName == name)
      .map(_.getConsoleTab)
      .toSeq
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

  Disposer.register(this, contentManager)

  override def dispose(): Unit = {}
}

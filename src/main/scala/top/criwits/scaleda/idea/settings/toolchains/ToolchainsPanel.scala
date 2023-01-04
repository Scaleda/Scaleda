package top.criwits.scaleda
package idea.settings.toolchains

import kernel.toolchain.{Toolchain, ToolchainProfile}

import com.intellij.openapi.actionSystem.{ActionManager, ActionPlaces, ActionPopupMenu, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.components.{JBList, JBPanelWithEmptyText}
import com.intellij.ui.{AnActionButton, ColoredListCellRenderer, ToolbarDecorator}
import com.intellij.util.ui.JBUI
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.settings.toolchains.panel.SinglePathConfigPanel
import top.criwits.scaleda.idea.utils.MainLogger
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.awt.BorderLayout
import javax.swing.event.ListSelectionEvent
import javax.swing._
import scala.annotation.nowarn
import scala.collection.mutable

/**
 * Settings panel for toolchains
 */
class ToolchainsPanel extends JPanel(new BorderLayout) {
  // Left side, list
  private val listModel = new MyListModel
  private val toolchainList = new JBList[ToolchainProfile](listModel)
  toolchainList.setCellRenderer(new MyCellRenderer)
  toolchainList.addListSelectionListener(onItemSelected)

  // Splitter
  val splitter = new Splitter(false, 0.3f)
  add(splitter, BorderLayout.CENTER)

  val listPanel: JPanel = ToolbarDecorator.createDecorator(toolchainList)
    .setAddAction((e: AnActionButton) => addProfile(e))
    .setRemoveAction((_: AnActionButton) => removeProfile())
    .disableUpDownActions()
    .createPanel()

  splitter.setFirstComponent(listPanel)

  val emptyPanel: JBPanelWithEmptyText = new JBPanelWithEmptyText()
    .withEmptyText(ScaledaBundle.message("settings.no_tool_chain"))
    .withBorder(JBUI.Borders.emptyLeft(6))
  splitter.setSecondComponent(emptyPanel)

  var profiles: mutable.Seq[ToolchainProfile] = _


  private def init(): Unit = {
    profiles = Toolchain.profiles()
    listModel.clear()
    profiles.foreach(p => listModel.addElement(p))
    if (profiles.nonEmpty) {
      toolchainList.setSelectedIndex(0)
      loadItem(profiles.head)
    }
  }

  private def onItemSelected(e: ListSelectionEvent): Unit = {
    val index = toolchainList.getSelectedIndex
    if (index >= 0 && index < profiles.size) {
      val profile = listModel.get(index)
      loadItem(profile)
    }
  }

  private def loadItem(profile: ToolchainProfile): Unit = splitter.setSecondComponent(profile.toolchainType match {
    case _ => new SinglePathConfigPanel(profile).getComponent
  })


  private def addProfile(e: AnActionButton): Unit = {
    val group = new DefaultActionGroup()
    Toolchain.toolchains.foreach(m =>
    group.add(new AnAction(m._2._1) {
      override def actionPerformed(e: AnActionEvent): Unit = {
        val newProfile = new ToolchainProfile(m._2._1, m._1, "")
        newProfile.edited = true
        profiles :+= newProfile
        listModel.addElement(newProfile)
        toolchainList.setSelectedIndex(listModel.indexOf(newProfile))
      }
    }))
    val popupMenu = ActionManager.getInstance().createActionPopupMenu(ActionPlaces.POPUP, group)
    popupMenu.getComponent.show(this, e.getContextComponent.getX, e.getContextComponent.getY)
  }

  private def removeProfile(): Unit = {
    val index = toolchainList.getSelectedIndex
    if (index >= 0 && index < profiles.size) {
      val profile = listModel.get(index)
      profile.edited = true
      profile.removed = true
      listModel.remove(index)
      if (index != 0) {
        toolchainList.setSelectedIndex(index - 1)
      } else {
        splitter.setSecondComponent(emptyPanel)
      }
    }
  }

  def modified(): Boolean = if (profiles.isEmpty) false else profiles.map(p => p.edited).reduce(_ || _)
  def reset(): Unit = { init() }
  def apply(): Unit = profiles.foreach(p =>  if (p.edited) {
    Toolchain.syncProfile(p)
    p.edited = false
  } )


  private class MyListModel extends DefaultListModel[ToolchainProfile]
  private class MyCellRenderer extends ColoredListCellRenderer[ToolchainProfile] {
    override def customizeCellRenderer(list: JList[_ <: ToolchainProfile],
                                       value: ToolchainProfile,
                                       index: Int,
                                       selected: Boolean,
                                       hasFocus: Boolean): Unit =
      append(s"${value.profileName} [${Toolchain.toolchains(value.toolchainType)._1}]")

  }

}

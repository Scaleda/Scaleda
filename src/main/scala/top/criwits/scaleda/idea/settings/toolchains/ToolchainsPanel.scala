package top.criwits.scaleda
package idea.settings.toolchains

import kernel.toolchain.{Toolchain, ToolchainProfile}

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{ActionManager, ActionPlaces, ActionPopupMenu, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.components.{JBList, JBPanelWithEmptyText}
import com.intellij.ui.{AnActionButton, AnimatedIcon, ColoredListCellRenderer, ToolbarDecorator}
import com.intellij.util.ui.JBUI
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.settings.toolchains.panel.SinglePathConfigPanel
import top.criwits.scaleda.idea.utils.MainLogger
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.awt.BorderLayout
import java.awt.event.{ActionEvent, ActionListener}
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
    profiles = Toolchain.profiles(cache = false)
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

  private def loadItem(profile: ToolchainProfile): Unit = {
    val panel = profile.toolchainType match {
      case _ => new SinglePathConfigPanel(profile)
    }
    def verify(): Unit = {
      panel.getStatusLabel.clear()
      panel.getStatusLabel.setIcon(AnimatedIcon.Default.INSTANCE)
      panel.getStatusLabel.append(ScaledaBundle.message("settings.verifying"))

      val (status, version) = profile.verify()

      if (status == 0) {
        panel.getStatusLabel.clear()
        panel.getStatusLabel.setIcon(AllIcons.General.InspectionsOK)
        panel.getStatusLabel.append(version.get)
      } else {
        panel.getStatusLabel.clear()
        panel.getStatusLabel.setIcon(AllIcons.General.BalloonError)
        panel.getStatusLabel.append(status match {
          case -1 => ScaledaBundle.message("settings.invalid_path")
          case _ => ScaledaBundle.message("settings.unknown_error")
        })
      }
    }
    panel.getToolchainPathField.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        verify()
      }
    })

    splitter.setSecondComponent(panel.getComponent)
  }


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

      // some verbose logic
      if (listModel.size() == 0) {
        splitter.setSecondComponent(emptyPanel)
      } else {
        // eg. index = 2, original size = 3, current size = 2
        if (index == listModel.size()) {
          toolchainList.setSelectedIndex(index - 1)
        } else {
          toolchainList.setSelectedIndex(index)
        }
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

package top.criwits.scaleda
package idea.settings.toolchains

import kernel.toolchain.{Toolchain, ToolchainProfile}

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{ActionManager, ActionPlaces, ActionPopupMenu, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.components.{JBList, JBPanelWithEmptyText}
import com.intellij.ui.{AnActionButton, AnimatedIcon, ColoredListCellRenderer, DocumentAdapter, ToolbarDecorator}
import com.intellij.util.ui.JBUI
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.settings.toolchains.panel.SinglePathConfigPanel
import top.criwits.scaleda.idea.utils.MainLogger
import top.criwits.scaleda.kernel.shell.command.{CommandResponse, CommandRunner}
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.awt.BorderLayout
import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import javax.swing.event.{DocumentEvent, ListSelectionEvent}
import javax.swing._
import scala.annotation.nowarn
import scala.async.Async.async
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
      def updateStatusLabel(icon: Icon, @Nls str: String) = {
        SwingUtilities.invokeLater(() => {
          panel.getStatusLabel.clear()
          panel.getStatusLabel.setIcon(icon)
          panel.getStatusLabel.append(str)
        })
      }

      updateStatusLabel(AnimatedIcon.Default.INSTANCE, ScaledaBundle.message("settings.verifying"))

      val verifier = profile.getVerifier
      verifier match {
        case None =>
          updateStatusLabel(AllIcons.General.ExclMark, ScaledaBundle.message("settings.unsupported_toolchain"))
        case Some(v) =>
          // Has valid verifier
          v.verifyCommandLine match {
            case None =>
              updateStatusLabel(AllIcons.General.BalloonError, ScaledaBundle.message("settings.invalid_path"))
            case Some(cmdLine) =>
              // here has a command line
              var outputString = ""
              CommandRunner.execute(Seq(cmdLine), (r, d) => r match {
                case CommandResponse.Stdout | CommandResponse.Stderr => outputString += s"${d.asInstanceOf[String]}\n"
                case CommandResponse.Return =>
                  val retVal = d.asInstanceOf[Int]
                  val result = v.parseVersionInfo(retVal, outputString)

                  result match {
                    case (true, Some(version)) =>
                      updateStatusLabel(AllIcons.General.InspectionsOK, version) // NonNls to Nls
                    case (_, _) =>
                      updateStatusLabel(AllIcons.General.BalloonError, ScaledaBundle.message("settings.not_found"))
                  }
              })

          }
      }
    }

    panel.getToolchainPathField.getTextField.getDocument.addDocumentListener(new DocumentAdapter {
      override def textChanged(e: DocumentEvent): Unit = {
        new Thread(() => verify()).start()
      }
    })

    splitter.setSecondComponent(panel.getComponent)
    verify()
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

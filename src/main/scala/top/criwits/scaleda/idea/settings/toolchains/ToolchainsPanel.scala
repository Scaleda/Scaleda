package top.criwits.scaleda
package idea.settings.toolchains

import idea.ScaledaBundle
import idea.settings.toolchains.panel.{AbstractConfigPanel, IVerilogConfigPanel, SinglePathConfigPanel}
import kernel.shell.ScaledaRunHandler
import kernel.shell.command.CommandRunner
import kernel.toolchain.{Toolchain, ToolchainProfile}

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.ui.Splitter
import com.intellij.ui._
import com.intellij.ui.components.{JBList, JBPanelWithEmptyText}
import com.intellij.util.ui.JBUI
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.kernel.utils.Paths

import java.awt.BorderLayout
import java.util.concurrent.Executors
import javax.swing._
import javax.swing.event.{DocumentEvent, ListSelectionEvent}
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

//  // Path panel
//  val pathPanel = new JPanel(new BorderLayout)
//  pathPanel.add(new JLabel("Profiles are stored at"), BorderLayout.WEST)
//  val pathField = new JTextField(Paths.getToolchainsDir.getAbsolutePath)
//  pathField.setEditable(false)
//  pathPanel.add(pathField, BorderLayout.CENTER)
//  add(pathPanel, BorderLayout.NORTH)

  // Splitter
  val splitter = new Splitter(false, 0.3f)
  add(splitter, BorderLayout.CENTER)

  val listPanel: JPanel = ToolbarDecorator.createDecorator(toolchainList)
    .setAddAction((e: AnActionButton) => addProfile(e))
    .setRemoveAction((_: AnActionButton) => removeProfile())
    .disableUpDownActions()
    .createPanel()

  splitter.setFirstComponent(listPanel)

  // Right side, default panel is empty with 'No toolchain configured'
  val emptyPanel: JBPanelWithEmptyText = new JBPanelWithEmptyText()
    .withEmptyText(ScaledaBundle.message("settings.toolchains.no_profile"))
    .withBorder(JBUI.Borders.emptyLeft(6))
  splitter.setSecondComponent(emptyPanel)

  var profiles: mutable.Seq[ToolchainProfile] = _

  // Thread pool for profile validate
  private val executor = Executors.newSingleThreadExecutor()

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
    val panel: AbstractConfigPanel = profile.toolchainType match {
      case "iverilog" => new IVerilogConfigPanel(profile)
      case _ => new SinglePathConfigPanel(profile)
    }

    def verify(): Unit = {
      def updateStatusLabel(icon: Icon, @Nls str: String): Unit = {
        if (!Thread.interrupted()) { // if interrupted then not modifying, not used
          SwingUtilities.invokeLater(() => {
            panel.getStatusLabel.clear()
            panel.getStatusLabel.setIcon(icon)
            panel.getStatusLabel.append(str)
          })
        }
      }

      updateStatusLabel(AnimatedIcon.Default.INSTANCE, ScaledaBundle.message("settings.toolchains.verifying"))

      val verifier = profile.getVerifier
      verifier match {
        case None =>
          updateStatusLabel(AllIcons.General.ExclMark, ScaledaBundle.message("settings.toolchains.unsupported"))
        case Some(v) =>
          // Has valid verifier
          v.verifyCommandLine match {
            case None =>
              updateStatusLabel(AllIcons.General.BalloonError, ScaledaBundle.message("settings.toolchains.invalid_path"))
            case Some(cmdLine) =>
              // here has a command line
              var outputStrings: Seq[String] = Seq.empty
              var returnValues: Seq[Int] = Seq.empty
              CommandRunner.execute(cmdLine, new ScaledaRunHandler {
                override def onStdout(data: String): Unit = outputStrings ++= Seq(s"$data")
                override def onStderr(data: String): Unit = onStdout(data) // hum?
                override def onReturn(returnValue: Int): Unit = returnValues ++= Seq(returnValue)
              }, ignoreErrors = true) // ignore errors, for iverilog usage will return `1`

              val result = v.parseVersionInfo(returnValues, outputStrings)

              result match {
                case (true, Some(version)) =>
                  updateStatusLabel(AllIcons.General.InspectionsOK, version) // NonNls to Nls
                case (_, _) =>
                  updateStatusLabel(AllIcons.General.BalloonError, ScaledaBundle.message("settings.toolchains.not_found"))
              }
          }
      }
    }
    def executeVerify(): Unit = executor.execute(() => verify())

    panel.addPathFieldListener(new DocumentAdapter {
      override def textChanged(e: DocumentEvent): Unit = executeVerify()
    })

    splitter.setSecondComponent(panel.getComponent)
    executeVerify()
  }


  private def addProfile(e: AnActionButton): Unit = {
    val group = new DefaultActionGroup()
    Toolchain.toolchains.foreach(m =>
      group.add(new AnAction(m._2._1) { // FIXME: strange
        override def actionPerformed(e: AnActionEvent): Unit = {
          val newProfile = new ToolchainProfile(m._2._1, m._1)
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

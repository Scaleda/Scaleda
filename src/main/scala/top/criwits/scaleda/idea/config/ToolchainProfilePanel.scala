package top.criwits.scaleda
package kernel.toolchain.config

import com.intellij.openapi.ui.Splitter
import com.intellij.ui.{AnActionButton, AnActionButtonRunnable, ColoredListCellRenderer, ToolbarDecorator}
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import top.criwits.scaleda.idea.config.ToolchainSettingsPanel
import top.criwits.scaleda.kernel.toolchain.ToolchainProfile
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.awt.BorderLayout
import java.io.File
import java.util.UUID
import javax.swing.event.ListSelectionEvent
import javax.swing.{DefaultListModel, JList, JPanel}
import scala.annotation.nowarn
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

class ToolchainProfilePanel extends JPanel(new BorderLayout) {
  // Right side, setting panel
  private val mySettingsPanel = new ToolchainSettingsPanel

  // Left side, list
  private val myListModel = new MyListModel
  private val myList = new JBList[ToolchainProfile](myListModel)

  private var mySelectedProfile: ToolchainProfile = _

  // Storage
  private def profiles = ToolchainProfiles.getInstance.getProfiles().getLinkedExternalProjectsSettings

  // Init panel
  initPanel()
  // Load items
  reloadItems()
  private def initPanel(): Unit = {
    val splitter = new Splitter(false, 0.3f)
    add(splitter, BorderLayout.CENTER)

    @nowarn("cat=deprecation")
    val listPanel = ToolbarDecorator.createDecorator(myList)
      .setAddAction((_: AnActionButton) => addProfile())
      .createPanel()
    myList.addListSelectionListener(onItemSelected)
    splitter.setFirstComponent(listPanel)

    myList.setCellRenderer(new MyCellRenderer)

    val settingsComponent = mySettingsPanel.getComponent
    settingsComponent.setBorder(JBUI.Borders.emptyLeft(6))
    splitter.setSecondComponent(settingsComponent)

    // test
  }

  private def onItemSelected(e: ListSelectionEvent): Unit = {

  }

  private def reloadItems(): Unit = {
    myListModel.clear()
//    profiles.zipWithIndex.foreach(m => myListModel.add(m._2, m._1))
  }

  private def addProfile(): Unit = {

  }

  private class MyListModel extends DefaultListModel[ToolchainProfile] {

  }

  private class MyCellRenderer extends ColoredListCellRenderer[ToolchainProfile] {
    override def customizeCellRenderer(list: JList[_ <: ToolchainProfile], value: ToolchainProfile, index: Int, selected: Boolean,
                                       hasFocus: Boolean): Unit = {
    }

  }

}

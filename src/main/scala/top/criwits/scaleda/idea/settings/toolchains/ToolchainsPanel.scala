package top.criwits.scaleda
package idea.settings.toolchains

import kernel.toolchain.ToolchainProfile

import com.intellij.openapi.ui.Splitter
import com.intellij.ui.components.JBList
import com.intellij.ui.{AnActionButton, ColoredListCellRenderer, ToolbarDecorator}
import com.intellij.util.ui.JBUI
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.awt.BorderLayout
import javax.swing.event.ListSelectionEvent
import javax.swing._
import scala.annotation.nowarn

class ToolchainsPanel extends JPanel(new BorderLayout) {
  // Right side, setting panel
  private val mySettingsPanel: JComponent = new JLabel("Test")

  // Left side, list
  private val myListModel = new MyListModel
  private val myList = new JBList[ToolchainProfile](myListModel)

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

    val settingsComponent = mySettingsPanel
    settingsComponent.setBorder(JBUI.Borders.emptyLeft(6))
    splitter.setSecondComponent(settingsComponent)

    // test
    myListModel.add(0, new Vivado.Profile("1!5!", "HI"))
  }

  private def onItemSelected(e: ListSelectionEvent): Unit = {

  }

  private def reloadItems(): Unit = {
    myListModel.clear()
//    profiles.zipWithIndex.foreach(m => myListModel.add(m._2, m._1))
  }

  private def addProfile(): Unit = {

  }

  private class MyListModel extends DefaultListModel[ToolchainProfile]


  private class MyCellRenderer extends ColoredListCellRenderer[ToolchainProfile] {
    override def customizeCellRenderer(list: JList[_ <: ToolchainProfile], value: ToolchainProfile, index: Int, selected: Boolean,
                                       hasFocus: Boolean): Unit = append(value.profileName)

  }

}

package top.criwits.scaleda
package idea.windows.tasks.ip

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.{AnActionButton, ColoredListCellRenderer, ToolbarDecorator}
import com.intellij.ui.components.{JBList, JBPanelWithEmptyText}
import com.intellij.util.ui.JBUI
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.kernel.project.config.ProjectConfig

import java.awt.BorderLayout
import javax.swing.{DefaultListModel, JList, JPanel}

class ScaledaIPManagerPanel(val project: Project) extends JPanel(new BorderLayout) {
  // Left side, list
  val listModel = new DefaultListModel[IPInstance]
  val ipInstanceList = new JBList[IPInstance](listModel)
  ipInstanceList.setCellRenderer(new MyCellRenderer)
  ipInstanceList.addListSelectionListener(null) // TODO

  // Splitter
  val splitter = new Splitter(false, 0.3f)
  add(splitter, BorderLayout.CENTER)

  val listPanel: JPanel = ToolbarDecorator.createDecorator(ipInstanceList)
    .setAddAction((e: AnActionButton) => addIP(e))
    .setRemoveAction((_: AnActionButton) => {})
    .disableUpDownActions()
    .createPanel()

  splitter.setFirstComponent(listPanel)

  // Right side, default panel is a JBPanelWithEmptyText with 'No IP configured'
  val emptyPanel: JBPanelWithEmptyText = new JBPanelWithEmptyText()
    .withEmptyText(ScaledaBundle.message("windows.ip.noip"))
    .withBorder(JBUI.Borders.emptyLeft(6))
  splitter.setSecondComponent(emptyPanel)


  private def addIP(e: AnActionButton): Unit = {
    val projIP = ProjectConfig.projectBasicIps()
    val libIP = ProjectConfig.libraryIps
    val ipList = (projIP ++ libIP).map(s => IP(s._1, s._2))
    val r = new ScaledaChooseIPDialog(project, ipList)
    val _r = r.showAndGet()

    val selectedIP = r.mySelectedIP
  }

  private class MyCellRenderer extends ColoredListCellRenderer[IPInstance] {
    override def customizeCellRenderer(list: JList[_ <: IPInstance], value: IPInstance, index: Int, selected: Boolean, hasFocus: Boolean): Unit = {
      append(value.name)
    }
  }
}

package top.criwits.scaleda
package idea.windows.tasks.ip

import idea.ScaledaBundle
import kernel.project.config.ProjectConfig

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.components.{JBList, JBPanelWithEmptyText, JBTextField}
import com.intellij.ui.{AnActionButton, ColoredListCellRenderer, DocumentAdapter, ToolbarDecorator}
import com.intellij.util.ui.{FormBuilder, JBUI, UIUtil}

import java.awt.BorderLayout
import javax.swing.event.{DocumentEvent, ListSelectionEvent}
import javax.swing.{DefaultListModel, JList, JPanel}

class ScaledaIPManagerPanel(val project: Project) extends JPanel(new BorderLayout) {
  private val projIP = ProjectConfig.projectBasicIps()
  private val libIP  = ProjectConfig.libraryIps
  private val ipList = (projIP ++ libIP).map(s => IP(s._1, s._2))
  // Left side, list
  private val listModel      = new DefaultListModel[IPInstance]
  private val ipInstanceList = new JBList[IPInstance](listModel)
  ipInstanceList.setCellRenderer(new MyCellRenderer)
  ipInstanceList.addListSelectionListener(null) // TODO

  // Splitter
  val splitter = new Splitter(false, 0.3f)
  add(splitter, BorderLayout.CENTER)

  val listPanel: JPanel = ToolbarDecorator
    .createDecorator(ipInstanceList)
    .setAddAction((e: AnActionButton) => addIP(e))
    .setRemoveAction((_: AnActionButton) => {})
    .disableUpDownActions()
    .createPanel()

  splitter.setFirstComponent(listPanel)

  // Right side, default panel is a JBPanelWithEmptyText with 'No IP selected'
  val emptyPanel: JBPanelWithEmptyText = new JBPanelWithEmptyText()
    .withEmptyText(ScaledaBundle.message("windows.ip.no.ip.selected"))
    .withBorder(JBUI.Borders.emptyLeft(6))
  splitter.setSecondComponent(emptyPanel)

  private def addIP(e: AnActionButton): Unit = {

    val r  = new ScaledaChooseIPDialog(project, ipList)
    val _r = r.showAndGet()

    val selectedIP = r.mySelectedIP
    selectedIP.foreach(ip => {
      listModel.addElement(
        new IPInstance(
          ip.config.exports.get.module,
          ip.config.exports.get.id,
          ip.config.exports.get.options.map(e => (e.name, e.default)).toMap
        )
      )
    })
  }

  private def onItemSelected(e: ListSelectionEvent): Unit = {
    val index = ipInstanceList.getSelectedIndex
    if (index >= 0 && index < listModel.size()) {
      val ip = listModel.get(index)
      loadItem(ip)
    }
  }

  private def loadItem(item: IPInstance): Unit = {
    // check IP model
    val ips = ipList.filter(_.config.exports.get.id == item.typeId)
    if (ips.isEmpty) return // TODO: shall never reach here...
    val ip = ips.head

    // Build UI
    val form = FormBuilder.createFormBuilder().setAlignLabelOnRight(false).setHorizontalGap(UIUtil.DEFAULT_HGAP)
    .setVerticalGap(UIUtil.DEFAULT_VGAP)
    ip.config.exports.get.options.foreach(option => {
      option.`type` match {
        case "string" =>
          val field = new JBTextField(item.options(option.name).asInstanceOf[String])
          field.getDocument.addDocumentListener(new DocumentAdapter {
            override def textChanged(e: DocumentEvent): Unit = item.options.put(option.name, field.getText)
          })
          form.addLabeledComponent(option.name, field) // FIXME: Nls

        case "int" =>
          form.addLabeledComponent(option.name, new JBTextField(item.options(option.name).asInstanceOf[Int].toString)) // FIXME: Nls
      }
    })

  }

  private class MyCellRenderer extends ColoredListCellRenderer[IPInstance] {
    override def customizeCellRenderer(
        list: JList[_ <: IPInstance],
        value: IPInstance,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean
    ): Unit = {
      append(value.module)
    }
  }
}

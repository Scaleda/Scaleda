package top.criwits.scaleda
package idea.windows.tasks.ip

import idea.ScaledaBundle

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.TableView
import com.intellij.uiDesigner.core.{GridConstraints, GridLayoutManager}

import java.awt.Dimension
import javax.swing.event.ListSelectionEvent
import javax.swing.{JComponent, JPanel, ListSelectionModel}

class ScaledaChooseIPDialog(project: Project, ips: Seq[IP]) extends DialogWrapper(project) {

  private val myTable      = new TableView[IP]
  private val myTableModel = new IPTableModel

  private var mySelectedIP: Option[IP] = None

  locally {
    setTitle(ScaledaBundle.message("windows.ip.choose.title"))
    setResizable(true)
    init()
    ips.foreach(ip => {
      myTableModel.addRow(ip)
      myTableModel.fireTableDataChanged()
    })

  }

  override def createCenterPanel(): JComponent = {
    val panel = new JPanel

    myTable.setModelAndUpdateColumns(myTableModel)
    myTable.getSelectionModel.addListSelectionListener((_: ListSelectionEvent) => updateOKButtonEnabled())
    myTable.getColumnModel.getSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    updateOKButtonEnabled()

    val scrollPane = new JBScrollPane
    scrollPane.setViewportView(myTable)

    panel.setLayout(new GridLayoutManager(1, 1))
    panel.add(
      scrollPane,
      new GridConstraints(
        0,
        0,
        1,
        1,
        GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
        null,
        new Dimension(-1, 500),
        null,
        0,
        false
      )
    )
    panel
  }

  private def updateOKButtonEnabled(): Unit = {
    val isIPSelected = myTable.getSelectedRow >= 0
    setOKActionEnabled(isIPSelected)
  }

  private def selectedIPFromTable: Option[IP] = {
    if (myTable.getSelectedRowCount > 0) {
      Some(myTableModel.getItems.get(myTable.getSelectedRow))
    } else None
  }

  override def doOKAction(): Unit = {
    mySelectedIP = selectedIPFromTable
    super.doOKAction()
    dispose()
  }
}

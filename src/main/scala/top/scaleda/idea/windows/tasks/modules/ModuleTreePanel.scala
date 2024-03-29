package top.scaleda
package idea.windows.tasks.modules

import idea.utils.Icons
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.utils.FileUtils

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import com.intellij.ui.{ColoredListCellRenderer, ListSpeedSearch, ToolbarDecorator, TreeUIHelper}

import java.awt.BorderLayout
import javax.swing.event.ListSelectionEvent
import javax.swing.{DefaultListModel, JList, JPanel}

class ModuleTreePanel(project: Project, dialog: SelectModuleDialogWrapper) extends JPanel {
  setLayout(new BorderLayout)

  val modules: Array[ModuleDeclarationPsiNode] =
    FileUtils.getAllVerilogFiles(project).map(_.getModuleDeclarations).foldLeft(Seq[ModuleDeclarationPsiNode]())(_ ++ _).toArray

  val listModel = new DefaultListModel[ModuleDeclarationPsiNode]
  modules.foreach(module => listModel.addElement(module))

  val myList = new JBList(listModel)

  //noinspection ConvertExpressionToSAM
  val colouredListCellRenderer: ColoredListCellRenderer[ModuleDeclarationPsiNode] =
    new ColoredListCellRenderer[ModuleDeclarationPsiNode] {
      override def customizeCellRenderer(
          list: JList[_ <: ModuleDeclarationPsiNode],
          value: ModuleDeclarationPsiNode,
          index: Int,
          selected: Boolean,
          hasFocus: Boolean
      ): Unit = {
        setIcon(Icons.verilogModule)
        append(value.getName)
      }
    }

  myList.setCellRenderer(colouredListCellRenderer)

  TreeUIHelper.getInstance().installListSpeedSearch(myList)

  val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(myList)
    .disableRemoveAction()
    .disableUpDownActions()

  add(decorator.createPanel(), BorderLayout.CENTER)

  def getSelectedModule: ModuleDeclarationPsiNode = myList.getSelectedValue

  myList.addListSelectionListener((e: ListSelectionEvent) => {
    dialog.selectedModule = getSelectedModule
    if (getSelectedModule != null) dialog.setOKActionEnabled(true)
    else dialog.setOKActionEnabled(false)
  })

}

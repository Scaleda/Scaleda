package top.criwits.scaleda
package idea.windows.tasks

import com.intellij.openapi.ui.Splitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.JBUI
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.windows.tasks.project.ScaledaEditProjectPanelWrapper
import top.criwits.scaleda.idea.windows.tasks.target.ScaledaEditTargetPanelWrapper
import top.criwits.scaleda.idea.windows.tasks.task.ScaledaEditTaskPanelWrapper

import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.event.TreeSelectionEvent
import javax.swing.tree.DefaultTreeModel

class ScaledaEditTasksPanel(val scaledaRunRootNode: ScaledaRunRootNode, setValid: Boolean => Unit) extends JPanel(new BorderLayout) {
  private val model = new DefaultTreeModel(scaledaRunRootNode)
  // left side, tree
  private val tree = new Tree(model)
  tree.setCellRenderer(new ScaledaRunTreeCellRenderer)
  tree.addTreeSelectionListener(onItemSelected)
  private val treePanel = ToolbarDecorator.createDecorator(tree)
    .setAddAction(null) // todo later
    .setRemoveAction(null) // todo later
    .disableUpDownActions()
    .createPanel()

  // Right side, default panel
  private val emptyPanel = new JBPanelWithEmptyText()
    .withEmptyText(ScaledaBundle.message("windows.edit.empty"))
    .withBorder(JBUI.Borders.emptyLeft(6))


  // Splitter
  val splitter = new Splitter(false, 0.3f)
  add(splitter, BorderLayout.CENTER)
  splitter.setFirstComponent(treePanel)
  splitter.setSecondComponent(emptyPanel)

  private def onItemSelected(e: TreeSelectionEvent): Unit = {
    // check which node is selected
    val rootNode = tree.getSelectedNodes(classOf[ScaledaRunRootNode], (_: ScaledaRunRootNode) => true)
    val targetNode = tree.getSelectedNodes(classOf[ScaledaRunTargetNode], (_: ScaledaRunTargetNode) => true)
    val taskNode = tree.getSelectedNodes(classOf[ScaledaRunTaskNode], (_: ScaledaRunTaskNode) => true)

    if (rootNode.nonEmpty) splitter.setSecondComponent(new ScaledaEditProjectPanelWrapper(rootNode.head).getPanel)
    if (targetNode.nonEmpty) splitter.setSecondComponent(new ScaledaEditTargetPanelWrapper(targetNode.head).getPanel)
    if (taskNode.nonEmpty) splitter.setSecondComponent(new ScaledaEditTaskPanelWrapper(taskNode.head).getPanel)
  }

  model.reload()

}

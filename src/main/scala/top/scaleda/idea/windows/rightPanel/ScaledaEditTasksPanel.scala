package top.scaleda
package idea.windows.rightPanel

import idea.ScaledaBundle
import idea.project.IdeaManifestManager
import idea.windows.rightPanel.project.ScaledaEditProjectPanelWrapper
import idea.windows.rightPanel.target.ScaledaEditTargetPanelWrapper
import idea.windows.rightPanel.task.ScaledaEditTaskPanelWrapper
import kernel.project.config.{TargetConfig, TaskConfig}

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.treeStructure.Tree
import com.intellij.ui.{AnActionButton, ToolbarDecorator}
import com.intellij.util.ui.JBUI
import top.scaleda.idea.windows.rightPanel.treeNodes.{ScaledaTasksRootNode, ScaledaTasksTargetNode, ScaledaTasksTaskNode}

import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.event.TreeSelectionEvent
import javax.swing.tree.DefaultTreeModel

/** Panel for task edit window
  * @param scaledaRunRootNode the root node
  * @param setValid a recall function for parent dialog to change 'ok' valid status
  */
class ScaledaEditTasksPanel(project: Project, val scaledaRunRootNode: ScaledaTasksRootNode, setValid: Boolean => Unit)
    extends JPanel(new BorderLayout) {
  implicit val manifest = IdeaManifestManager.getImplicitManifest(project = project)
  val model             = new DefaultTreeModel(scaledaRunRootNode)
  // left side, tree
  private val tree = new Tree(model)
  tree.setCellRenderer(new ScaledaTasksTreeCellRenderer)
  tree.addTreeSelectionListener(onItemSelected)
  tree.setExpandsSelectedPaths(true) //?
  private val decorator =
    ToolbarDecorator
      .createDecorator(tree)
      .setAddAction((e: AnActionButton) => addItem())
      .setRemoveAction((e: AnActionButton) => removeItem())
      .disableUpDownActions()

  private val treePanel = decorator
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
    val rootNode   = tree.getSelectedNodes(classOf[ScaledaTasksRootNode], (_: ScaledaTasksRootNode) => true)
    val targetNode = tree.getSelectedNodes(classOf[ScaledaTasksTargetNode], (_: ScaledaTasksTargetNode) => true)
    val taskNode   = tree.getSelectedNodes(classOf[ScaledaTasksTaskNode], (_: ScaledaTasksTaskNode) => true)

    if (rootNode.nonEmpty)
      splitter.setSecondComponent(new ScaledaEditProjectPanelWrapper(rootNode.head, updateOK()).getPanel)
    if (targetNode.nonEmpty)
      splitter.setSecondComponent(new ScaledaEditTargetPanelWrapper(targetNode.head, updateOK()).getPanel)
    if (taskNode.nonEmpty)
      splitter.setSecondComponent(new ScaledaEditTaskPanelWrapper(taskNode.head, updateOK()).getPanel)
  }

  private def addItem(): Unit = {
    // check which node is selected
    val rootNode   = tree.getSelectedNodes(classOf[ScaledaTasksRootNode], (_: ScaledaTasksRootNode) => true)
    val targetNode = tree.getSelectedNodes(classOf[ScaledaTasksTargetNode], (_: ScaledaTasksTargetNode) => true)
    val taskNode   = tree.getSelectedNodes(classOf[ScaledaTasksTaskNode], (_: ScaledaTasksTaskNode) => true)

    if (rootNode.nonEmpty) {
      // add target
      val node = new ScaledaTasksTargetNode(TargetConfig())
      node.parent = Some(rootNode.head)
      scaledaRunRootNode.targets.append(node)

      model.nodesWereInserted(scaledaRunRootNode, Array(scaledaRunRootNode.getIndex(node)))
      tree.setSelectionPath(node.toTreePath)
    }
    if (targetNode.nonEmpty) {
      // add task
      val node = new ScaledaTasksTaskNode(TaskConfig())
      node.parent = Some(targetNode.head)
      targetNode.head.tasks.append(node)

      model.nodesWereInserted(targetNode.head, Array(targetNode.head.getIndex(node)))
      tree.setSelectionPath(node.toTreePath)
    }
    if (taskNode.nonEmpty) {
      // add task
      val node = new ScaledaTasksTaskNode(TaskConfig())
      node.parent = Some(taskNode.head.getParent.asInstanceOf[ScaledaTasksTargetNode])
      taskNode.head.parent.get.tasks.append(node) // can it work?

      model.nodesWereInserted(taskNode.head.getParent, Array(taskNode.head.getParent.getIndex(node)))
      tree.setSelectionPath(node.toTreePath)
    }

  }

  private def removeItem() = {
    // check which node is selected
    val rootNode   = tree.getSelectedNodes(classOf[ScaledaTasksRootNode], (_: ScaledaTasksRootNode) => true)
    val targetNode = tree.getSelectedNodes(classOf[ScaledaTasksTargetNode], (_: ScaledaTasksTargetNode) => true)
    val taskNode   = tree.getSelectedNodes(classOf[ScaledaTasksTaskNode], (_: ScaledaTasksTaskNode) => true)

    if (targetNode.nonEmpty) {
      val target = targetNode.head
      val proj   = target.parent.get
      val idx    = proj.targets.indexOf(target)
      proj.targets.remove(idx)
      model.nodesWereRemoved(proj, Array(idx), Array(target))
    }

    if (taskNode.nonEmpty) {
      val task   = taskNode.head
      val target = task.parent.get
      val idx    = target.tasks.indexOf(task)
      target.tasks.remove(idx)
      model.nodesWereRemoved(target, Array(idx), Array(task))
    }
  }

  private def updateOK(): Unit = {
    setValid(scaledaRunRootNode.validate)
  }

  model.reload()

}

package top.criwits.scaleda
package idea.windows

import idea.ScaledaBundle
import kernel.project.config.{ProjectConfig, TaskConfig}

import com.intellij.openapi.externalSystem.service.task.ui.AbstractExternalSystemToolWindowFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.treeStructure.Tree

import java.awt.event.{MouseAdapter, MouseEvent, MouseListener}
import javax.swing.JTree
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeModel}

class ScaledaTargetWindowPanel(project: Project) extends SimpleToolWindowPanel(true, true) {
  // Tree
  private val tree = new Tree(new MyTreeModel)
  tree.setRootVisible(true) // FIXME
  tree.setDragEnabled(true)
  tree.setExpandableItemsEnabled(true)
  tree.setCellRenderer(new MyCellRenderer)


  private val root = tree.getModel.getRoot.asInstanceOf[RootNode]
  private val sims = new CategoryNode(("simulation", ScaledaBundle.message("windows.tasks.simulation")))
  private val synth = new CategoryNode(("synthesis", ScaledaBundle.message("windows.tasks.synthesis")))
  private val impl = new CategoryNode(("implementation", ScaledaBundle.message("windows.tasks.implementation")))
  root.add(sims)
  root.add(synth)
  root.add(impl)

  // FIXME
  tree.addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = {
      val selPath = tree.getPathForLocation(e.getX, e.getY)
      if (e.getClickCount == 2 && selPath != null) {
        val node = selPath.getLastPathComponent match {
          case n: RootNode =>
          case n: CategoryNode =>
          case n: TargetNode =>
            println(s"${n.target.name}") // <== Double Click here
        }
      }
    }
  })


  setContent(tree)

  loadTasks()

  def loadTasks(): Unit = {
    sims.removeAllChildren()
    synth.removeAllChildren()
    impl.removeAllChildren()
    val runConfigs = ProjectConfig.getConfig()
    runConfigs match {
      case Some(config) =>
        config.tasks.foreach(task => {
          task.`type` match {
            case "simulation" => sims.add(new TargetNode(task))
            case "synthesis" => synth.add(new TargetNode(task))
            case "implementation" => impl.add(new TargetNode(task))
            case _ =>
          }
        })
      case None =>
    }
  }


  private class RootNode extends DefaultMutableTreeNode {
    // TODO
  }

  private class CategoryNode(val category: (String, String)) extends DefaultMutableTreeNode(category) {
    // TODO
  }

  private class TargetNode(val target: TaskConfig) extends DefaultMutableTreeNode(target) {
    setAllowsChildren(false)
  }

  private class MyCellRenderer extends ColoredTreeCellRenderer {
    override def customizeCellRenderer(tree: JTree, value: Any, selected: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean): Unit =
      value match {
        case node: CategoryNode =>
          append(node.category._2)
        case node: TargetNode =>
          append(node.target.name)
        case _ =>
      }
  }

  private class MyTreeModel extends DefaultTreeModel(new RootNode) {

  }


}

package top.scaleda
package idea.windows.rightPanel

import com.intellij.ui.{ColoredTreeCellRenderer, SimpleTextAttributes}
import top.scaleda.idea.windows.rightPanel.treeNodes.{ScaledaTasksRootNode, ScaledaTasksTargetNode, ScaledaTasksTaskNode, ScaledaTasksTreeNode}
import top.scaleda.kernel.project.config.TaskConfig
import top.scaleda.kernel.toolchain.Toolchain

import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeCellRenderer, TreeCellRenderer}

//noinspection ScalaExtractStringToBundle,ReferencePassedToNls
class ScaledaTasksTreeCellRenderer extends ColoredTreeCellRenderer {
  override def customizeCellRenderer(
      tree: JTree,
      value: Any,
      selected: Boolean,
      expanded: Boolean,
      leaf: Boolean,
      row: Int,
      hasFocus: Boolean
  ): Unit = {
    val node = value.asInstanceOf[ScaledaTasksTreeNode]
    setIcon(node.icon)
    append(node.name)

    node match {
      case node: ScaledaTasksRootNode =>
      case node: ScaledaTasksTargetNode =>
        append(s" [${
          Toolchain.toolchains.get(node.toolchain) match {
            case Some(v) => v._1
            case _ => "unknown"
          }
        }]", SimpleTextAttributes.GRAY_ATTRIBUTES)
      case node: ScaledaTasksTaskNode =>
        append(s" [${
          TaskConfig.taskTypeList.get(node.`type`) match {
            case Some(v) => v._1
            case _ => "unknown"
          }
        }]", SimpleTextAttributes.GRAY_ATTRIBUTES)
    }
  }
}

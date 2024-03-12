package top.scaleda
package idea.windows.tasks

import com.intellij.ui.{ColoredTreeCellRenderer, SimpleTextAttributes}
import top.scaleda.kernel.project.config.TaskConfig
import top.scaleda.kernel.toolchain.Toolchain

import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeCellRenderer, TreeCellRenderer}

class ScaledaRunTreeCellRenderer extends ColoredTreeCellRenderer {
  override def customizeCellRenderer(
      tree: JTree,
      value: Any,
      selected: Boolean,
      expanded: Boolean,
      leaf: Boolean,
      row: Int,
      hasFocus: Boolean
  ): Unit = {
    val node = value.asInstanceOf[ScaledaRunTreeNode]
    setIcon(node.icon)
    append(node.name)

    node match {
      case node: ScaledaRunRootNode =>
      case node: ScaledaRunTargetNode =>
        append(s" [${
          Toolchain.toolchains.get(node.toolchain) match {
            case Some(v) => v._1
            case _ => "unknown"
          }
        }]", SimpleTextAttributes.GRAY_ATTRIBUTES)
      case node: ScaledaRunTaskNode =>
        append(s" [${
          TaskConfig.taskTypeList.get(node.`type`) match {
            case Some(v) => v._1
            case _ => "unknown"
          }
        }]", SimpleTextAttributes.GRAY_ATTRIBUTES)
    }
  }
}

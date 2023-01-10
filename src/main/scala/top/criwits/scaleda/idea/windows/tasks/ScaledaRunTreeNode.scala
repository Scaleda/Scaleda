package top.criwits.scaleda
package idea.windows.tasks

import javax.swing.Icon
import javax.swing.tree.TreeNode

abstract class ScaledaRunTreeNode(val name: String) extends TreeNode {
  val icon: Icon
}

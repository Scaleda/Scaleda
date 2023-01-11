package top.criwits.scaleda
package idea.windows.tasks

import com.intellij.icons.AllIcons
import top.criwits.scaleda.kernel.project.config.TaskConfig

import java.util
import javax.swing.Icon
import javax.swing.tree.TreeNode

class ScaledaRunTaskNode(val task: TaskConfig)
    extends ScaledaRunTreeNode(task.name) {
  override val icon: Icon = AllIcons.RunConfigurations.TestState.Run

  var parent: Option[ScaledaRunTargetNode] = None

  override def getChildAt(i: Int): TreeNode = null

  override def getChildCount: Int = 0

  override def getParent: TreeNode = parent.get

  override def getIndex(treeNode: TreeNode): Int = 0

  override def getAllowsChildren: Boolean = false

  override def isLeaf: Boolean = true

  override def children(): util.Enumeration[_ <: TreeNode] = null
}

package top.criwits.scaleda
package idea.windows.tasks
import idea.utils.Icons

import java.util
import javax.swing.Icon
import javax.swing.tree.TreeNode
import scala.jdk.javaapi.CollectionConverters

class ScaledaRunRootNode(projectName: String, targets: Seq[ScaledaRunTargetNode])
    extends ScaledaRunTreeNode(projectName) {
  targets.foreach(t => t.parent = Some(this))
  private val targetNodes = CollectionConverters.asJava(targets)

  override val icon: Icon = Icons.mainSmall

  override def getChildAt(i: Int): TreeNode = targetNodes.get(i)

  override def getChildCount: Int = targetNodes.size()

  override def getParent: TreeNode = null

  override def getIndex(treeNode: TreeNode): Int = targetNodes.indexOf(treeNode)

  override def getAllowsChildren: Boolean = true

  override def isLeaf: Boolean = false

  override def children(): util.Enumeration[_ <: TreeNode] = null
}

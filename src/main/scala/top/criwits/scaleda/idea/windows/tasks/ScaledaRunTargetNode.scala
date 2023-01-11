package top.criwits.scaleda
package idea.windows.tasks

import idea.utils.Icons
import top.criwits.scaleda.kernel.project.config.TargetConfig

import java.util
import java.util.Collections.enumeration
import javax.swing.tree.TreeNode
import scala.jdk.javaapi.CollectionConverters

class ScaledaRunTargetNode(val target: TargetConfig)
    extends ScaledaRunTreeNode(target.name) {
  override val icon = Icons.mainSmall

  var parent: Option[ScaledaRunRootNode] = None

  private val taskNodes =
    CollectionConverters.asJava(target.tasks.map(t => {
      val n = new ScaledaRunTaskNode(t)
      n.parent = Some(this)
      n
    }))

  override def getChildAt(i: Int): TreeNode = taskNodes.get(i)

  override def getChildCount: Int = taskNodes.size()

  override def getParent: TreeNode = parent.get

  override def getIndex(treeNode: TreeNode): Int = taskNodes.indexOf(treeNode)

  override def getAllowsChildren: Boolean = true

  override def isLeaf: Boolean = false

  override def children(): util.Enumeration[_ <: TreeNode] = enumeration(
    taskNodes
  )
}

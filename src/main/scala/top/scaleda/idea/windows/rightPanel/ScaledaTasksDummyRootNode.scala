package top.scaleda
package idea.windows.rightPanel
import java.util
import javax.swing.Icon
import javax.swing.tree.TreeNode
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._

class ScaledaTasksDummyRootNode(roots: Seq[ScaledaRunRootNode] = Seq[ScaledaRunRootNode]()) extends ScaledaRunTreeNode("Dummy Root") {
  override val icon: Icon                  = null
  override var topModule: Option[String]   = None
  override var constraints: Option[String] = None

  val rootNodes: mutable.ArrayBuffer[ScaledaRunRootNode] = ArrayBuffer.from(roots)
  rootNodes.foreach(_.parent = Some(this))

  override def validate: Boolean = true

  override def getChildAt(childIndex: Int): TreeNode = rootNodes(childIndex)

  override def getChildCount: Int = rootNodes.length

  override def getParent: TreeNode = null

  override def getIndex(node: TreeNode): Int = rootNodes.indexOf(node)

  override def getAllowsChildren: Boolean = true

  override def isLeaf: Boolean = false

  override def children(): util.Enumeration[ScaledaRunRootNode] = util.Collections.enumeration(rootNodes.asJava)

}

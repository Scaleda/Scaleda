package top.scaleda
package idea.windows.rightPanel.treeNodes

import javax.swing.Icon
import javax.swing.tree.{TreeNode, TreePath}

abstract class ScaledaTasksTreeNode(var name: String) extends TreeNode {
  val icon: Icon
  var topModule: Option[String]
  var constraints: Seq[String]

  def constraintsEditable: Boolean = constraints.size <= 1

  def findTopModule: Option[String] = {
    topModule match {
      case Some(str) => Some(str)
      case None =>
        getParent match {
          case parent: ScaledaTasksTreeNode => parent.findTopModule
          case null                         => None
        }
    }
  }

  // TODO: use Seq[String] instead of Option[File] in editor
  def findConstraints: Option[String] = {
    constraints.headOption match {
      case Some(str) => Some(str)
      case None =>
        getParent match {
          case parent: ScaledaTasksTreeNode => parent.findConstraints
          case null                         => None
        }
    }
  }

  def validate: Boolean

  def toTreePath: TreePath = {
    if (this.isInstanceOf[ScaledaTasksRootNode]) new TreePath(this)
    else new TreePath(this.getParent.asInstanceOf[ScaledaTasksTreeNode].toTreePath, this)
  }

  override def toString = name
}

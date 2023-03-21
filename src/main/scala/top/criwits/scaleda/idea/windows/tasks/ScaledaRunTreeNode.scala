package top.criwits.scaleda
package idea.windows.tasks

import javax.swing.Icon
import javax.swing.tree.{TreeNode, TreePath}

abstract class ScaledaRunTreeNode(var name: String) extends TreeNode {
  val icon: Icon
  var topModule: Option[String]
  var constraints: Option[String]

  def findTopModule: Option[String] = {
    topModule match {
      case Some(str) => Some(str)
      case None => getParent match {
        case parent: ScaledaRunTreeNode => parent.findTopModule
        case null => None
      }
    }
  }

  def findConstraints: Option[String] = {
    constraints match {
      case Some(str) => Some(str)
      case None => getParent match {
        case parent: ScaledaRunTreeNode => parent.findConstraints
        case null => None
      }
    }
  }

  def validate: Boolean

  def toTreePath: TreePath = {
    if (this.isInstanceOf[ScaledaRunRootNode]) new TreePath(this)
    else new TreePath(this.getParent.asInstanceOf[ScaledaRunTreeNode].toTreePath, this)
  }

  override def toString = name
}

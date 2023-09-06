package top.scaleda
package idea.windows.rightPanel.treeNodes

import idea.utils.Icons
import kernel.project.config.TaskConfig
import kernel.utils.KernelFileUtils

import java.util
import javax.swing.Icon
import javax.swing.tree.TreeNode

class ScaledaTasksTaskNode(val task: TaskConfig) extends ScaledaTasksTreeNode(task.name) {
  override val icon: Icon = Icons.task

  var parent: Option[ScaledaTasksTargetNode] = None

  override def getChildAt(i: Int): TreeNode = null

  override def getChildCount: Int = 0

  override def getParent: TreeNode = parent.orNull

  override def getIndex(treeNode: TreeNode): Int = 0

  override def getAllowsChildren: Boolean = false

  override def isLeaf: Boolean = true

  override def children(): util.Enumeration[_ <: TreeNode] = null

  override var topModule: Option[String]   = task.topModule
  override var constraints: Option[String] = task.constraints
  var `type`: String                       = task.`type`

  def toTaskConfig: TaskConfig = TaskConfig(
    name = name,
    `type` = `type`,
    topModule = topModule
  )

  override def validate: Boolean = {
    KernelFileUtils.isLegalName(name) &&
      findTopModule.isDefined &&
      (`type` match {
        case "implementation" => findConstraints.isDefined
        case _ => true
      })
  }
}

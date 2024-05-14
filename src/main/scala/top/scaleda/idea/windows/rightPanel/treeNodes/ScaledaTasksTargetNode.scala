package top.scaleda
package idea.windows.rightPanel.treeNodes

import idea.utils.Icons
import kernel.project.config.TargetConfig
import kernel.toolchain.impl.Vivado
import kernel.utils.KernelFileUtils

import java.util
import java.util.Collections.enumeration
import javax.swing.tree.TreeNode
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class ScaledaTasksTargetNode(val target: TargetConfig) extends ScaledaTasksTreeNode(target.name) {
  override val icon = Icons.target

  var parent: Option[ScaledaTasksRootNode] = None

  override def getChildAt(i: Int): TreeNode = tasks(i)

  override def getChildCount: Int = tasks.size

  override def getParent: TreeNode = parent.orNull

  override def getIndex(treeNode: TreeNode): Int = tasks.indexOf(treeNode)

  override def getAllowsChildren: Boolean = true

  override def isLeaf: Boolean = false

  override def children(): util.Enumeration[_ <: TreeNode] = enumeration(
    tasks.asJava
  )

  override var topModule: Option[String]   = target.topModule
  override var constraints: Seq[String]    = target.constraintPaths
  var toolchain: String                    = target.toolchain
  var options: Option[Map[String, String]] = target.options

  var tasks: mutable.Buffer[ScaledaTasksTaskNode] = target.tasks.map(new ScaledaTasksTaskNode(_)).toBuffer
  tasks.foreach(_.parent = Some(this))

  def toTargetConfig: TargetConfig = TargetConfig(
    name,
    toolchain,
    topModule,
    constraints,
    options,
    tasks.map(_.toTaskConfig).toArray
  )

  override def validate: Boolean = {
    KernelFileUtils.isLegalName(name) &&
    (toolchain match {
      case Vivado.internalID =>
        options.nonEmpty &&
          options.get.contains("part")
      // todo: add more?
      case _ => true
    }) && (tasks.isEmpty || tasks.forall(_.validate))
  }
}

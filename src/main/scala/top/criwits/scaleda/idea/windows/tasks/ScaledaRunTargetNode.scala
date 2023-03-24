package top.criwits.scaleda
package idea.windows.tasks

import idea.utils.Icons
import kernel.project.config.TargetConfig

import top.criwits.scaleda.kernel.toolchain.impl.Vivado
import top.criwits.scaleda.kernel.utils.KernelFileUtils

import java.util
import java.util.Collections.enumeration
import javax.swing.tree.TreeNode
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class ScaledaRunTargetNode(val target: TargetConfig) extends ScaledaRunTreeNode(target.name) {
  override val icon = Icons.target

  var parent: Option[ScaledaRunRootNode] = None

  override def getChildAt(i: Int): TreeNode = tasks(i)

  override def getChildCount: Int = tasks.size

  override def getParent: TreeNode = parent.get

  override def getIndex(treeNode: TreeNode): Int = tasks.indexOf(treeNode)

  override def getAllowsChildren: Boolean = true

  override def isLeaf: Boolean = false

  override def children(): util.Enumeration[_ <: TreeNode] = enumeration(
    tasks.asJava
  )

  override var topModule: Option[String]   = target.topModule
  override var constraints: Option[String] = target.constraints
  var toolchain: String                    = target.toolchain
  var options: Option[Map[String, String]] = target.options

  var tasks: mutable.Buffer[ScaledaRunTaskNode] = target.tasks.map(new ScaledaRunTaskNode(_)).toBuffer
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
    }) && (tasks.isEmpty || tasks.map(_.validate).reduce(_ && _))
  }
}

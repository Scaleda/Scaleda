package top.criwits.scaleda
package idea.windows.tasks
import idea.utils.Icons
import kernel.project.config.ProjectConfig

import top.criwits.scaleda.kernel.utils.KernelFileUtils

import java.util
import javax.swing.Icon
import javax.swing.tree.TreeNode
import java.util.Collections.enumeration
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class ScaledaRunRootNode(val projectConfig: ProjectConfig)
    extends ScaledaRunTreeNode(projectConfig.name) {
  override val icon: Icon = Icons.mainSmall
  override def getChildAt(i: Int): TreeNode = targets(i)
  override def getChildCount: Int = targets.size
  override def getParent: TreeNode = null
  override def getIndex(treeNode: TreeNode): Int = targets.indexOf(treeNode)
  override def getAllowsChildren: Boolean = true
  override def isLeaf: Boolean = false
  override def children(): util.Enumeration[_ <: TreeNode] = enumeration(targets.asJava)

  override var topModule: Option[String] = projectConfig.topModule
  override var constraints: Option[String] = projectConfig.constraints
  var `type`: String = projectConfig.`type`
  var source: String = projectConfig.source
  var test: String = projectConfig.test

  var targets: mutable.Buffer[ScaledaRunTargetNode] = projectConfig.targets.map(new ScaledaRunTargetNode(_)).toBuffer
  targets.foreach(_.parent = Some(this))

  def toProjectConfig: ProjectConfig = ProjectConfig(
    name,
    `type`,
    source,
    test,
    topModule,
    constraints,
    targets.map(_.toTargetConfig).toArray
  )

  override def validate: Boolean = {
    KernelFileUtils.isLegalName(name) && !source.isBlank && !test.isBlank && targets.map(_.validate).reduce(_ && _)
  }
}

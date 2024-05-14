package top.scaleda
package idea.windows.rightPanel.treeNodes

import idea.utils.Icons
import kernel.project.config.ProjectConfig
import kernel.utils.KernelFileUtils

import java.util
import java.util.Collections.enumeration
import javax.swing.Icon
import javax.swing.tree.TreeNode
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class ScaledaTasksRootNode(val projectConfig: ProjectConfig) extends ScaledaTasksTreeNode(projectConfig.name) {
  var parent: Option[ScaledaTasksDummyRootNode]            = None
  override val icon: Icon                                  = Icons.mainSmall
  override def getChildAt(i: Int): TreeNode                = targets(i)
  override def getChildCount: Int                          = targets.size
  override def getParent: TreeNode                         = parent.orNull
  override def getIndex(treeNode: TreeNode): Int           = targets.indexOf(treeNode)
  override def getAllowsChildren: Boolean                  = true
  override def isLeaf: Boolean                             = false
  override def children(): util.Enumeration[_ <: TreeNode] = enumeration(targets.asJava)

  override var topModule: Option[String] = projectConfig.topModule
  override var constraints: Seq[String]  = projectConfig.constraintPaths
  var `type`: String                     = projectConfig.`type`
  var source: String                     = projectConfig.source
  // TODO: configure ProjectConfig.sources
  var test: String = projectConfig.test

  var targets: mutable.Buffer[ScaledaTasksTargetNode] =
    projectConfig.targets.map(new ScaledaTasksTargetNode(_)).toBuffer
  targets.foreach(_.parent = Some(this))

  /** Re-generate project configuration
    * TODO: IP pass through
    * @return
    */
  def toProjectConfig: ProjectConfig = projectConfig.copy(
    name = name,
    // description, author, version
    `type` = `type`,
    source = source,
    // sources
    test = test,
    // tests
    topModule = topModule,
    constraintPaths = constraints,
    targets = targets.map(_.toTargetConfig).toArray
  )

  override def validate: Boolean = {
    KernelFileUtils.isLegalName(name) && !source.isBlank && !test.isBlank &&
    (targets.isEmpty || targets.forall(_.validate))
  }
}

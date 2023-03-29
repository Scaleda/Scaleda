package top.criwits.scaleda
package kernel.project.config

import kernel.utils.KernelFileUtils

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class ConfigNode() {
  @JsonIgnore
  val topModule: Option[String]
  @JsonIgnore
  val constraints: Option[String]
  @JsonIgnore
  var parentNode: Option[ConfigNode] = None
  @JsonIgnore
  val source: String = "src/"
  @JsonIgnore
  val sources: Seq[String] = Seq()
  @JsonIgnore
  val test: String = "src/"
  @JsonIgnore
  val tests: Seq[String] = Seq()

  @JsonIgnore
  def findTopModule: Option[String] = topModule match {
    case None =>
      parentNode match {
        case Some(parent) => parent.findTopModule
        case None         => None
      }
    case Some(str) => Some(str)
  }

  @JsonIgnore
  def findConstraints: Option[String] = constraints match {
    case Some(str) => Some(str)
    case None      => parentNode.flatMap(_.findConstraints)
  }

  @JsonIgnore
  private def parseAsAbsolutePath(path: String, projectBase: Option[String] = ProjectConfig.projectBase) =
    KernelFileUtils.toAbsolutePath(path, projectBase = projectBase).getOrElse(path)

  /** Get all source set. Default is project base.
    * @return sources in absolute path
    */
  @JsonIgnore
  def getSourceSet(projectBase: Option[String] = None): Set[String] = {
    val base = if (projectBase.nonEmpty) projectBase else ProjectConfig.projectBase
    (parentNode match {
      case Some(parent) => parent.getSourceSet(projectBase = base)
      case None         => Set()
    }) ++ (if (source.nonEmpty) Set(parseAsAbsolutePath(source)) else Set()) ++
      sources.filter(_.nonEmpty).map(parseAsAbsolutePath(_))
  }

  /** Get testbench source set. Default is project base.
    * @return testbench in absolute path
    */
  @JsonIgnore
  def getTestSet(projectBase: Option[String] = None): Set[String] = {
    val base = if (projectBase.nonEmpty) projectBase else ProjectConfig.projectBase
    (parentNode match {
      case Some(parent) => parent.getTestSet(projectBase = base)
      case None         => Set()
    }) ++ (if (test.nonEmpty) Set(parseAsAbsolutePath(test)) else Set()) ++
      tests.filter(_.nonEmpty).map(parseAsAbsolutePath(_))
  }
}

package top.criwits.scaleda
package kernel.project.config

import kernel.utils.KernelFileUtils

import com.fasterxml.jackson.annotation.JsonIgnore

import java.io.File

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
  val ipFiles: Seq[String] = Seq()
  @JsonIgnore
  val ipPaths: Seq[String] = Seq()

  /** Get top module name
    * @return top module name, may not exist
    */
  @JsonIgnore
  def findTopModule: Option[String] = topModule match {
    case None =>
      parentNode match {
        case Some(parent) => parent.findTopModule
        case None         => None
      }
    case Some(str) => Some(str)
  }

  /** Get constraints file or directories
    * @return constraints file or directories
    */
  @JsonIgnore
  def getConstraints: Option[String] = constraints match {
    case Some(str) => Some(str)
    case None      => parentNode.flatMap(_.getConstraints)
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
    }) ++ (if (source.nonEmpty) Set(parseAsAbsolutePath(source, projectBase = base)) else Set()) ++
      sources.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = base))
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
    }) ++ (if (test.nonEmpty) Set(parseAsAbsolutePath(test, projectBase = base)) else Set()) ++
      tests.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = base))
  }

  /** Get all Simple Target IP files
    * @return simple target ip files or search path in absolute path
    */
  @JsonIgnore
  def getIpFiles(projectBase: Option[String] = None): Set[String] = {
    val base = if (projectBase.nonEmpty) projectBase else ProjectConfig.projectBase
    (parentNode match {
      case Some(parent) => parent.getIpFiles(projectBase = base)
      case None         => Set()
    }) ++ ipFiles.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = base))
  }

  /** Get Scaleda IP search path, including basic paths: .ip, ip, ips
    * @return ip search paths
    */
  @JsonIgnore
  def getIpPaths(projectBase: Option[String] = None): Set[String] = {
    val base       = if (projectBase.nonEmpty) projectBase else ProjectConfig.projectBase
    val basicPaths = Set(".ip", "ip", "ips")
    basicPaths ++ (parentNode match {
      case Some(parent) => parent.getIpPaths(projectBase = base)
      case None         => Set()
    }) ++ ipPaths.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = base))
  }

  /** Get all Scaleda IP [[ProjectConfig]]
    * @return map of ip abs-path and [[ProjectConfig]]
    */
  @JsonIgnore
  def getIps(projectBase: Option[String] = None): Map[String, ProjectConfig] = {
    val base  = if (projectBase.nonEmpty) projectBase else ProjectConfig.projectBase
    val paths = getIpPaths(projectBase = projectBase)
    // search just one layer
    paths
      .map(parseAsAbsolutePath(_, projectBase = base))
      .map(new File(_))
      .map(path => (path, KernelFileUtils.parseIpDirectory(path)))
      .filter(_._2.nonEmpty)
      .map(p => (p._1.getAbsolutePath, p._2.get))
      .toMap
  }
}

package top.criwits.scaleda
package kernel.project.config

import kernel.utils.KernelFileUtils
import kernel.utils.KernelFileUtils.parseAsAbsolutePath

import com.fasterxml.jackson.annotation.JsonIgnore

import java.io.File
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

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
  @JsonIgnore
  val ips: Map[String, Map[String, Any]] = Map()

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

  /** Get all source set. Default is project base.
    * @return sources in absolute path
    */
  @JsonIgnore
  def getSourceSet(@JsonIgnore projectBase: Option[String] = ProjectConfig.projectBase): Set[String] = {
    (parentNode match {
      case Some(parent) => parent.getSourceSet(projectBase = projectBase)
      case None         => Set()
    }) ++ (if (source.nonEmpty) Set(parseAsAbsolutePath(source, projectBase = projectBase)) else Set()) ++
      sources.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = projectBase))
  }

  /** Get testbench source set. Default is project base.
    * @return testbench in absolute path
    */
  @JsonIgnore
  def getTestSet(@JsonIgnore projectBase: Option[String] = ProjectConfig.projectBase): Set[String] = {
    (parentNode match {
      case Some(parent) => parent.getTestSet(projectBase = projectBase)
      case None         => Set()
    }) ++ (if (test.nonEmpty) Set(parseAsAbsolutePath(test, projectBase = projectBase)) else Set()) ++
      tests.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = projectBase)) ++
      getSourceSet(projectBase = projectBase)
  }

  /** Get all Simple Target IP files
    * @return simple target ip files or search path in absolute path
    */
  @JsonIgnore
  def getIpFiles(@JsonIgnore projectBase: Option[String] = ProjectConfig.projectBase): Set[String] = {
    (parentNode match {
      case Some(parent) => parent.getIpFiles(projectBase = projectBase)
      case None         => Set()
    }) ++ ipFiles.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = projectBase))
  }

  /** Get Scaleda IP search path, including basic paths: .ip, ip, ips
    * @return ip search paths
    */
  @JsonIgnore
  def getIpPaths(@JsonIgnore projectBase: Option[String] = ProjectConfig.projectBase): Set[String] = {
    val basicPaths = ProjectConfig.projectIpPaths(projectBase = projectBase)
    basicPaths.map(_.getAbsolutePath) ++ (parentNode match {
      case Some(parent) => parent.getIpPaths(projectBase = projectBase)
      case None         => Set()
    }) ++ ipPaths.filter(_.nonEmpty).map(parseAsAbsolutePath(_, projectBase = projectBase))
  }

  /** Get defined Scaleda IP in this project, but not recursively from other IPs
    * @return map of ip abs-path and [[ProjectConfig]]
    */
  @JsonIgnore
  def getLocalIps(@JsonIgnore projectBase: Option[String] = ProjectConfig.projectBase): Map[String, ProjectConfig] = {
    val paths = getIpPaths(projectBase = projectBase)
    // search just one layer: .ips/<ip name>
    paths
      .map(new File(_))
      .filter(_.exists())
      .filter(_.isDirectory)
      .flatMap(p => KernelFileUtils.parseIpParentDirectory(p))
      .toMap
  }

  /** Recursively get ALL IPs from this project
    * @return map of ip abs-path and [[ProjectConfig]]
    */
  @JsonIgnore
  def getAllIps(@JsonIgnore projectBase: Option[String] = ProjectConfig.projectBase): Map[String, ProjectConfig] = {
    // base of this current project
    val localIps = getLocalIps(projectBase = projectBase)
    // BFS Search
    val q = mutable.Queue.empty[(String, ProjectConfig)]
    q ++= localIps
    // identifier is IP name, to avoid ring dependence
    val resultIpNames = new mutable.HashSet[String]()
    val results       = ArrayBuffer[(String, ProjectConfig)]()
    while (q.nonEmpty) {
      val top = q.dequeue()
      if (!resultIpNames.contains(top._2.exports.get.name)) {
        resultIpNames += top._2.exports.get.name
        results += top
        // only search more ips in ProjectConfig
        q ++= top._2.getLocalIps(projectBase = Some(top._1))
      }
    }
    results.toMap
  }

  /** Get IP Instances
    * @return name and context
    */
  @JsonIgnore
  def getIpInstances(@JsonIgnore projectBase: Option[String] = ProjectConfig.projectBase): Map[String, Map[String, Any]] = {
    ((parentNode match {
      case Some(parent) => parent.getIpInstances(projectBase = projectBase)
      case None         => Map()
    }) ++ ips).toMap
  }

  // TODO: get ip instances recursively
  // @JsonIgnore
  // def getAllIpInstances(projectBase: Option[String] = None): Map[String, Map[String, Any]] = {
  // }
}

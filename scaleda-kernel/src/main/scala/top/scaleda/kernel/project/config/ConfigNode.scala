package top.scaleda
package kernel.project.config

import kernel.project.ProjectManifest
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
  val source: String
  @JsonIgnore
  val sources: Seq[String]
  @JsonIgnore
  val test: String
  @JsonIgnore
  val tests: Seq[String]
  @JsonIgnore
  val ipFiles: Seq[String]
  @JsonIgnore
  val ipPaths: Seq[String]
  @JsonIgnore
  val ips: Seq[IPInstance]

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
  def getSourceSet(implicit manifest: ProjectManifest): Set[String] = {
    (parentNode match {
      case Some(parent) => parent.getSourceSet
      case None         => Set()
    }) ++ (if (source.nonEmpty) Set(parseAsAbsolutePath(source)) else Set()) ++
      sources.filter(_.nonEmpty).map(parseAsAbsolutePath(_))
  }

  /** Get testbench source set. Default is project base.
    * @return testbench in absolute path
    */
  @JsonIgnore
  def getTestSet(implicit manifest: ProjectManifest): Set[String] = {
    (parentNode match {
      case Some(parent) => parent.getTestSet
      case None         => Set()
    }) ++ (if (test.nonEmpty) Set(parseAsAbsolutePath(test)) else Set()) ++
      tests.filter(_.nonEmpty).map(parseAsAbsolutePath(_)) ++
      getSourceSet
  }

  /** Get all Simple Target IP files
    * @return simple target ip files or search path in absolute path
    */
  @JsonIgnore
  def getIpFiles(implicit manifest: ProjectManifest): Set[String] = {
    (parentNode match {
      case Some(parent) => parent.getIpFiles
      case None         => Set()
    }) ++ ipFiles.filter(_.nonEmpty).map(parseAsAbsolutePath(_))
  }

  /** Get Scaleda IP search path, including basic paths: .ip, ip, ips
    * @return ip search paths
    */
  @JsonIgnore
  def getIpPaths(implicit manifest: ProjectManifest): Set[String] = {
    val basicPaths = ProjectConfig.projectIpPaths ++ ProjectConfig.libraryIpPaths
    basicPaths.map(_.getAbsolutePath) ++ (parentNode match {
      case Some(parent) => parent.getIpPaths
      case None         => Set()
    }) ++ ipPaths.filter(_.nonEmpty).map(parseAsAbsolutePath(_))
  }

  /** Get defined Scaleda IP in this project, but not recursively from other IPs
    * @return map of ip abs-path and [[ProjectConfig]]
    */
  @JsonIgnore
  def getLocalIps(implicit manifest: ProjectManifest): Map[String, ProjectConfig] = {
    val paths = getIpPaths
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
  def getAllIps(implicit manifest: ProjectManifest): Map[String, ProjectConfig] = {
    // base of this current project
    val localIps = getLocalIps
    // BFS Search
    val q = mutable.Queue.empty[(String, ProjectConfig)]
    q ++= localIps
    // identifier is IP name, to avoid ring dependence
    val resultIpIds = new mutable.HashSet[String]()
    val results     = ArrayBuffer[(String, ProjectConfig)]()
    while (q.nonEmpty) {
      val top = q.dequeue()
      if (!resultIpIds.contains(top._2.exports.get.id)) {
        resultIpIds += top._2.exports.get.id
        results += top
        // only search more ips in ProjectConfig
        q ++= top._2.getLocalIps(manifest = ProjectManifest.getTemporalManifest(top._1))
      }
    }
    results.toMap
  }

  /** Get IP Instances
    * @return name and context
    */
  @JsonIgnore
  def getIpInstances(implicit manifest: ProjectManifest): Seq[IPInstance] = {
    (parentNode match {
      case Some(parent) => parent.getIpInstances
      case None         => Seq()
    }) ++ ips.map(i =>
      new IPInstance(
        module = i.module,
        typeId = i.typeId,
        options = if (i.options != null && i.options.nonEmpty) i.options else mutable.Map()
      )
    )
  }

  // TODO: get ip instances recursively
  // @JsonIgnore
  // def getAllIpInstances(projectBase: Option[String] = None): Map[String, Map[String, Any]] = {
  // }
}

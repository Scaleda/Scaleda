package top.scaleda
package kernel.project.config

import kernel.project.ScaledaProject
import kernel.utils.KernelFileUtils
import kernel.utils.KernelFileUtils.parseAsAbsolutePath

import com.fasterxml.jackson.annotation.JsonIgnore

import java.io.File
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

abstract class ConfigNode() {
  @JsonIgnore
  val topModule: Option[String]
  // cannot be `constraints`... why???
  @JsonIgnore
  val constraintPaths: Seq[String]
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
  @JsonIgnore
  val cwd: Option[String]

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
  def getConstraints: Seq[String] =
    constraintPaths ++ parentNode.map(_.getConstraints).getOrElse(Seq())

  @JsonIgnore
  def getConstraintFiles(project: ScaledaProject): Set[File] =
    constraintPaths
      .map(p => {
        val f1 = new File(p)
        if (f1.exists()) Some(f1)
        else {
          val f2 = new File(project.projectBase.getOrElse(""), p)
          if (f2.exists()) Some(f2)
          else None
        }
      })
      .map {
        case Some(file) =>
          if (file.isDirectory)
            file.listFiles().toSeq
          else Seq(file)
        case None => Seq()
      }
      .reduceOption(_ ++ _)
      .getOrElse(Seq())
      .toSet ++ parentNode.map(_.getConstraintFiles(project)).getOrElse(Set())

  /** Get all source set based on project base, with folders and files mixed
    * @return sources in absolute path
    */
  @JsonIgnore
  def getSourceSet(implicit project: ScaledaProject): Set[String] = {
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
  def getTestSet(implicit project: ScaledaProject): Set[String] = {
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
  def getIpFiles(implicit project: ScaledaProject): Set[String] = {
    (parentNode match {
      case Some(parent) => parent.getIpFiles
      case None         => Set()
    }) ++ ipFiles.filter(_.nonEmpty).map(parseAsAbsolutePath(_))
  }

  /** Get Scaleda IP search path, including basic paths: .ip, ip, ips
    * @return ip search paths
    */
  @JsonIgnore
  def getIpPaths(implicit project: ScaledaProject): Set[String] = {
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
  def getLocalIps(implicit project: ScaledaProject): Map[String, ProjectConfig] = {
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
  def getAllIps(implicit project: ScaledaProject): Map[String, ProjectConfig] = {
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
        q ++= top._2.getLocalIps(project = ScaledaProject.getTemporalProject(top._1))
      }
    }
    results.toMap
  }

  /** Get IP Instances
    * @return name and context
    */
  @JsonIgnore
  def getIpInstances(implicit project: ScaledaProject): Seq[IPInstance] = {
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

  @JsonIgnore
  def getWorkingDirectory(implicit project: ScaledaProject): File = {
    val base = project.projectBase match {
      case Some(p) => new File(p)
      case None    => new File(".")
    }
    cwd match {
      case Some(p) => new File(parseAsAbsolutePath(p))
      case None    => base
    }
  }
}

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
    KernelFileUtils.toAbsolutePath(path).getOrElse(path)

  /** Get all source set
    * @return sources in absolute path
    */
  @JsonIgnore
  def getSourceSet(projectBase: Option[String] = ProjectConfig.projectBase): Set[String] =
    (parentNode match {
      case Some(parent) => parent.getSourceSet(projectBase = projectBase)
      case None         => Set()
    }) ++ Set(parseAsAbsolutePath(source)) ++ sources.map(parseAsAbsolutePath(_))

  /** Get testbench source set
    * @return testbench in absolute path
    */
  @JsonIgnore
  def getTestSet(projectBase: Option[String] = ProjectConfig.projectBase): Set[String] =
    (parentNode match {
      case Some(parent) => parent.getTestSet(projectBase = projectBase)
      case None         => Set()
    }) ++ Set(parseAsAbsolutePath(test)) ++ tests.map(parseAsAbsolutePath(_))
}

package top.criwits.scaleda
package kernel.project.config

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
  def getSourceSet: Set[String] =
    (parentNode match {
      case Some(parent) => parent.getSourceSet
      case None         => Set()
    }) ++ Set(source) ++ sources

  @JsonIgnore
  def getTestSet: Set[String] =
    (parentNode match {
      case Some(parent) => parent.getTestSet
      case None         => Set()
    }) ++ Set(test) ++ tests
}

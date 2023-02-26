package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class ConfigNode() {
  @JsonIgnore
  val topModule: Option[String]
  @JsonIgnore
  var parentNode: Option[ConfigNode] = None

  @JsonIgnore
  def findTopModule: Option[String] = topModule match {
    case None => parentNode match {
      case Some(parent) => parent.findTopModule
      case None => None
    }
    case Some(str) => Some(str)
  }
}

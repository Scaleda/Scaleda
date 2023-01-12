package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}

abstract class ConfigNode() {
  @JsonIgnore
  val topModule: String
  @JsonIgnore
  var parentNode: Option[ConfigNode] = None

  @JsonIgnore
  def findTopModule: Option[String] = topModule match {
    case "" | null => parentNode match {
      case Some(parent) => parent.findTopModule
      case None => None
    }
    case str => Some(str)
  }
}

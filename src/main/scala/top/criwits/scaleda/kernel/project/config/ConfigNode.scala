package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}

abstract class ConfigNode {
  @JsonInclude(Include.NON_EMPTY)
  val topModule: String = ""
  @JsonInclude(Include.NON_EMPTY)
  val testbenchModule: String = ""

  @JsonIgnore
  var parentNode: Option[ConfigNode] = None

  @JsonIgnore
  def getTopModule: Option[String] = topModule match {
    case "" | null => parentNode match {
      case Some(parent) => parent.getTopModule
      case None => None
    }
    case _ => Some(topModule)
  }
}

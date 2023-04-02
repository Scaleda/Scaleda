package top.criwits.scaleda
package idea.windows.tasks.ip

import com.fasterxml.jackson.annotation.JsonIgnore

import scala.collection.mutable

class IPInstance(var module: String, val typeId: String, val options: mutable.Map[String, Any]) {
  @JsonIgnore
  def getRenderOptions: Map[String, Any] = (Map("module" -> module) ++ options.toMap).toMap

  // mutable.Map cannot directly be serialized
  def getOptions: Map[String, Any] = options.toMap
}

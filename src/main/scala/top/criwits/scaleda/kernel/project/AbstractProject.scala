package top.criwits.scaleda
package kernel.project

import kernel.utils.RunningPlatform

import scala.language.implicitConversions

case class AbstractProject(p: AnyRef) {
  def getBasePath: String = p match {
    case p: AnyRef if p != null && RunningPlatform.isInIdea =>
      p.getClass.getMethod("getBasePath").invoke(p).asInstanceOf[String]
    case _ => ""
  }

  def isDefault: Boolean = p == null
}

object AbstractProject {
  implicit def fromAnyRef(any: AnyRef): AbstractProject = AbstractProject(any)
  // implicit def fromAnyRefOptional(any: Option[AnyRef]): Option[AbstractProject] = any.map(a => AbstractProject(a))
  def default = AbstractProject(null)
}

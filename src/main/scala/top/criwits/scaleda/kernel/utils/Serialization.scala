package top.criwits.scaleda
package kernel.utils

import scala.jdk.javaapi.CollectionConverters

object Serialization {
  def getCCParams(cc: AnyRef) =
    cc.getClass.getDeclaredFields.foldLeft(Map.empty[String, Any]) { (a, f) =>
      f.setAccessible(true)
      f.get(cc) match {
        case s: Seq[_] =>
          a + (f.getName -> CollectionConverters.asJava(s))
        case _ =>
          a + (f.getName -> f.get(cc))
      }
    }
}

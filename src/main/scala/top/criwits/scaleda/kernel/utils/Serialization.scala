package top.criwits.scaleda
package kernel.utils

object Serialization {
  def getCCParams(cc: AnyRef) =
    cc.getClass.getDeclaredFields.foldLeft(Map.empty[String, Any]) { (a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(cc))
    }
}

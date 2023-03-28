package top.criwits.scaleda
package kernel.project.ip

case class ExportOption(
    name: String,
    `type`: String
    // more: ranges, selectable options...
)

object ExportOption {
  val availableTypes: Map[String, Class[_]] = Map(
    "string" -> classOf[String],
    "int"    -> classOf[Int],
    "float"  -> classOf[Float]
  )
}

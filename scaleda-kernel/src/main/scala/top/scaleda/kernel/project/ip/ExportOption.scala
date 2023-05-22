package top.scaleda
package kernel.project.ip

case class ExportOption(
    name: String,
    `type`: String,
    default: Any
    // more: ranges, selectable options...
)

object ExportOption {
  val availableTypes: Map[String, Class[_]] = Map(
    "string" -> classOf[String],
    "int"    -> classOf[Int],
    "float"  -> classOf[Float],
    "boolean"  -> classOf[Boolean],
  )
}

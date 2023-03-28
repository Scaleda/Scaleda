package top.criwits.scaleda
package kernel.project.ip

case class ExportConfig(
    name: String,
    module: String,
    options: Array[ExportOption] = Array(),
    actions: Map[String, ExportAction] = Map()
)

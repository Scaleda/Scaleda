package top.criwits.scaleda
package kernel.project.ip

case class ExportConfig(
    name: String,
    module: String,
    options: Array[ExportOption] = Array(),
    defaults: Map[String, Any] = Map(),
    actions: Map[String, ExportAction] = Map(),
    defines: Array[ExportDefineConfig] = Array()
) {
  def getDefineHeadCode: String = {
    defines
      .map { define =>
        s"`define ${define.define} ${define.value}"
      }
      .mkString("\n")
  }
}

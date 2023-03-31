package top.criwits.scaleda
package kernel.project.ip

case class ExportConfig(
    name: String,
    module: String,
    stub: String,
    options: Array[ExportOption] = Array(),
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

  def getOptionContextMap(data: Map[String, Any] = Map()): Map[String, Any] =
    options.map(t => t.name -> t.default).toMap ++ data

  def getModuleContextMap: Map[String, Any] = Map(
    "module" -> module,
    "name"   -> name
  )

  def getDefaultContextMap(data: Map[String, Any] = Map()): Map[String, Any] =
    getModuleContextMap ++ getOptionContextMap(data)
}

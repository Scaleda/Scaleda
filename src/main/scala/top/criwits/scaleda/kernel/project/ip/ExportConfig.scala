package top.criwits.scaleda
package kernel.project.ip

import kernel.template.Template

case class ExportConfig(
    name: String,
    module: String,
    stub: String = "",
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

  def getContextMap(context: Map[String, Any] = Map()): Map[String, Any] =
    getModuleContextMap ++ getOptionContextMap(context)

  def renderStub(context: Map[String, Any] = Map()) = {
    val contextUse = getContextMap(context)
    Template.render(stub, contextUse)
  }
}

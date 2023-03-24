package top.criwits.scaleda
package kernel.project.importer

import kernel.project.config.TargetConfig
import kernel.project.detect.VivadoProjectConfig
import kernel.toolchain.impl.Vivado
import kernel.utils.serialise.XMLHelper

import java.io.File

class VivadoTargetParser extends BasicTargetParser {
  override def parseAsTarget(path: File): TargetConfig = {
    val projectFile = path.listFiles((file, s) => s.endsWith(".xpr")).head
    val obj         = XMLHelper(projectFile, classOf[VivadoProjectConfig])
    val target = TargetConfig(
      name = "Vivado",
      toolchain = Vivado.internalID
    )
    target
  }
}

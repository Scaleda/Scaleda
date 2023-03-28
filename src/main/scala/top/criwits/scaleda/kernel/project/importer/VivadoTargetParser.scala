package top.criwits.scaleda
package kernel.project.importer

import kernel.project.config.TargetConfig
import kernel.project.detect.VivadoProjectConfig
import kernel.toolchain.impl.Vivado
import kernel.utils.ImplicitPathReplace
import kernel.utils.serialise.XMLHelper

import java.io.File

class VivadoTargetParser extends BasicTargetParser {
  override def parseAsTarget(path: File): TargetConfig = {
    val projectFile = path.listFiles((file, s) => s.endsWith(".xpr")).head
    val projectName = projectFile.getName.split("\\.").head
    val o           = XMLHelper(projectFile, classOf[VivadoProjectConfig])
    // $PSRCDIR/sim_1 => <projectBase>/<projectName>.srcs/sim_1
    val replace = new ImplicitPathReplace("", "", Some("(\\$PSRCDIR[/\\\\]?)"), Seq(s"$projectName.srcs/"))
    require(o.Version == "7")
    val target = TargetConfig(
      name = "Vivado",
      toolchain = Vivado.internalID,
      // add source directory
      sources = o.fileSets.filter(_.Type == "DesignSrcs").map(_.RelSrcDir).map(replace.doRegexReplace),
      tests = o.fileSets.filter(_.Type == "SimulationSrcs").map(_.RelSrcDir).map(replace.doRegexReplace)
    )
    target
  }
}

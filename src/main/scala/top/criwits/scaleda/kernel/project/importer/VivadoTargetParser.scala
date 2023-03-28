package top.criwits.scaleda
package kernel.project.importer

import kernel.project.config.TargetConfig
import kernel.project.detect.VivadoProjectConfig
import kernel.toolchain.impl.Vivado
import kernel.utils.serialise.XMLHelper
import kernel.utils.{ImplicitPathReplace, KernelFileUtils}
import verilog.utils.ModuleUtils

import java.io.File

class VivadoTargetParser extends BasicTargetParser {
  override def parseAsTarget(path: File): TargetConfig = {
    val projectFile = path.listFiles((file, s) => s.endsWith(".xpr")).head
    val projectName = projectFile.getName.split("\\.").head
    val o           = XMLHelper(projectFile, classOf[VivadoProjectConfig])
    val projectBase = o.Path
    // $PSRCDIR/sim_1 => <projectBase>/<projectName>.srcs/sim_1
    val replace = new ImplicitPathReplace("", "", Some("(\\$PSRCDIR[/\\\\]?)"), Seq(s"$projectName.srcs/"))
    val srcSets = o.fileSets.filter(_.Type == "DesignSrcs")
    val sources =
      (srcSets.flatMap(_.files.map(_.Path)) ++ srcSets.map(_.RelSrcDir))
        .filter(_.nonEmpty)
        .map(replace.doRegexReplace)
        .map(p => KernelFileUtils.toAbsolutePath(p, projectBase = Some(projectBase)).getOrElse(p))
    val tests = o.fileSets.filter(_.Type == "SimulationSrcs").map(_.RelSrcDir).map(replace.doRegexReplace)
    val top = srcSets
      .flatMap(_.config.find(_.Name == "TopModule").map(_.Val))
      .headOption
      .getOrElse(
        ModuleUtils.parseSourceSetTopModules(sources.toSet).headOption.getOrElse("")
      )
    require(o.Version == "7")
    val target = TargetConfig(
      name = "Vivado",
      toolchain = Vivado.internalID,
      // add source directory
      sources = sources,
      tests = tests,
      topModule = if (top.nonEmpty) Some(top) else None
    )
    target
  }
}

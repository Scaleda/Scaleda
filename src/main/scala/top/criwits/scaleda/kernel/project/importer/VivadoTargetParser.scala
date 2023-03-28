package top.criwits.scaleda
package kernel.project.importer

import kernel.project.config.{TargetConfig, TaskConfig}
import kernel.project.detect.VivadoProjectConfig
import kernel.toolchain.impl.Vivado
import kernel.utils.serialise.XMLHelper
import kernel.utils.{ImplicitPathReplace, KernelFileUtils}
import verilog.utils.ModuleUtils

import java.io.File

trait VivadoTargetParser extends BasicTargetParser {
  override def parseAsTarget(path: File): TargetConfig = {
    val projectFile = path.listFiles((file, s) => s.endsWith(".xpr")).head
    val projectName = projectFile.getName.split("\\.").head
    val o           = XMLHelper(projectFile, classOf[VivadoProjectConfig])
    val projectBase = o.Path
    // $PSRCDIR/sim_1 => <projectBase>/<projectName>.srcs/sim_1
    val replace = new ImplicitPathReplace("", "", Some("(\\$PSRCDIR[/\\\\]?)"), Seq(s"$projectName.srcs/"))
    val srcSets = o.fileSets.filter(_.Type == "DesignSrcs")
    val simSets = o.fileSets.filter(_.Type == "SimulationSrcs")
    val sources =
      (srcSets.flatMap(_.files.map(_.Path)) ++ srcSets.map(_.RelSrcDir))
        .filter(_.nonEmpty)
        .map(replace.doRegexReplace)
        .map(p => KernelFileUtils.toAbsolutePath(p, projectBase = Some(projectBase)).getOrElse(p))
    val tests = o.fileSets.filter(_.Type == "SimulationSrcs").map(_.RelSrcDir).map(replace.doRegexReplace)
    // options in xpr usually have TopModule item
    val top = srcSets
      .flatMap(_.config.find(_.Name == "TopModule").map(_.Val))
      .headOption
      .getOrElse(
        ModuleUtils.parseSourceSetTopModules(sources.toSet).headOption.getOrElse("")
      )
    val simTop = simSets
      .flatMap(_.config.find(_.Name == "TopModule").map(_.Val))
      .headOption
      .getOrElse(
        ModuleUtils.parseSourceSetTopModules(tests.toSet).headOption.getOrElse("")
      )
    val part = o.options.find(_.Name == "Part").get.Val
    require(o.Version == "7")
    val synthTask = new TaskConfig(name = "Synth", `type` = "synthesis", preset = true)
    val simTask = new TaskConfig(
      name = "Simulation",
      `type` = "simulation",
      topModule = Some(simTop),
      preset = true
    )
    val relativeSources =
      sources.map(p => KernelFileUtils.toProjectRelativePath(p, projectBase = Some(projectBase)).getOrElse(p))
    val relativeTests =
      tests.map(p => KernelFileUtils.toProjectRelativePath(p, projectBase = Some(projectBase)).getOrElse(p))

    val ips = KernelFileUtils
      .getAllSourceFiles(sources.toSet, suffixing = Set("xci", "xcix"))
      .map(_.getAbsolutePath)
      .map(p => KernelFileUtils.toProjectRelativePath(p, projectBase = Some(projectBase)).getOrElse(p))
    val target = TargetConfig(
      name = "Vivado",
      toolchain = Vivado.internalID,
      // add sources; use relative path; if path is single dir, set to source/test
      sources = (if (relativeSources.size > 1) relativeSources else Seq()) ++ ips,
      source = if (relativeSources.size == 1) relativeSources.head else "",
      tests = relativeTests,
      test = if (relativeTests.size == 1) relativeTests.head else "",
      topModule = if (top.nonEmpty) Some(top) else None,
      tasks = if (simTop.nonEmpty) Array(synthTask, simTask) else Array(synthTask),
      options = Some(Map("part" -> part))
    )
    target
  }
}

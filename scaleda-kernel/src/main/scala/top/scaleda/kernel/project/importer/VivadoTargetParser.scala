package top.scaleda
package kernel.project.importer

import kernel.project.ScaledaProject
import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig}
import kernel.project.detect.VivadoProjectConfig
import kernel.toolchain.impl.Vivado
import kernel.utils.serialise.XMLHelper
import kernel.utils.{KernelFileUtils, RegexReplace}
import verilog.utils.ModuleUtils

import java.io.File

trait VivadoTargetParser extends BasicTargetParser {
  override def parseAsProject(path: File): ProjectConfig = {
    val projectFile    = path.listFiles((file, s) => s.endsWith(".xpr")).head
    val projectName    = projectFile.getName.split("\\.").head
    val o              = XMLHelper(projectFile, classOf[VivadoProjectConfig])
    val projectXprFile = new File(o.Path)
    val projectBase    = projectFile.getParentFile.getAbsolutePath
    implicit val project: ScaledaProject = ScaledaProject.getTemporalProject(projectBase)
    // $PSRCDIR/sim_1 => <projectBase>/<projectName>.srcs/sim_1
    val replace = new RegexReplace(
      regexPatten = "(\\$PSRCDIR[/\\\\]?)",
      regexReplacements = Seq(s"$projectName.srcs/")
    )
    val srcSets = o.fileSets.filter(_.Type == "DesignSrcs")
    val simSets = o.fileSets.filter(_.Type == "SimulationSrcs")
    val sources =
      (srcSets.flatMap(_.files.map(_.Path)) ++ srcSets.map(_.RelSrcDir))
        .filter(_.nonEmpty)
        .map(c => replace.doRegexReplace(c))
        .map(p => KernelFileUtils.toCanonicalPath(p).getOrElse(p))
    val tests = o.fileSets.filter(_.Type == "SimulationSrcs").map(_.RelSrcDir).map(c => replace.doRegexReplace(c))
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
    val synthTask = new TaskConfig(name = "Synth", `type` = "synthesis", custom = false)
    val simTask = new TaskConfig(
      name = "Simulation",
      `type` = "simulation",
      topModule = Some(simTop),
      custom = false
    )
    val relativeSources =
      sources.map(p => KernelFileUtils.toProjectRelativePath(p).getOrElse(p))
    val relativeTests =
      tests.map(p => KernelFileUtils.toProjectRelativePath(p).getOrElse(p))

    val simpleIPFiles = KernelFileUtils
      // search from source set, just from directories
      .getAllSourceFiles(sources.toSet, suffix = Set("xci", "xcix"))
      .map(_.getAbsolutePath)
      .map(p => KernelFileUtils.toProjectRelativePath(p).getOrElse(p))
    val target = TargetConfig(
      name = "Vivado",
      toolchain = Vivado.internalID,
      topModule = if (top.nonEmpty) Some(top) else None,
      tasks = if (simTop.nonEmpty) Array(synthTask, simTask) else Array(synthTask),
      options = Some(Map("part" -> part))
    )
    ProjectConfig(
      name = projectName,
      // add sources; use relative path; if path is single dir, set to source/test
      sources = if (relativeSources.size > 1) relativeSources else Seq(),
      source = if (relativeSources.size == 1) relativeSources.head else "",
      tests = if (relativeTests.size > 1) relativeTests else Seq(),
      test = if (relativeTests.size == 1) relativeTests.head else "",
      ipFiles = simpleIPFiles,
      targets = Array(target)
    )
  }
}

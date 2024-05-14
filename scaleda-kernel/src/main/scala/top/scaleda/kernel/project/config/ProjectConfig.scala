package top.scaleda
package kernel.project.config

import kernel.project.ScaledaProject
import kernel.project.ip.ExportConfig
import kernel.utils.serialise.{JSONHelper, YAMLHelper}
import kernel.utils.{KernelFileUtils, KernelLogger, Paths}

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

import java.io.File
import scala.language.implicitConversions

@JsonInclude(Include.NON_EMPTY)
case class ProjectConfig(
    name: String = "default-project",
    description: String = "",
    version: String = "0.1",
    author: String = "",
    `type`: String = "rtl",
    source: String = "src/",
    sources: Seq[String] = Seq(),
    test: String = "test/",
    tests: Seq[String] = Seq(),
    topModule: Option[String] = None,
    constraintPaths: Seq[String] = Seq(),
    targets: Array[TargetConfig] = Array(),
    ipFiles: Seq[String] = Seq(),
    ipPaths: Seq[String] = Seq(),
    exports: Option[ExportConfig] = None,
    ips: Seq[IPInstance] = Seq(),
    cwd: Option[String] = None
) extends ConfigNode() {
  def targetsWithSim =
    targets.filter(t => t.tasks.exists(t => t.`type` == "simulation"))

  def targetsWithSynth =
    targets.filter(t => t.tasks.exists(t => t.`type` == "synthesis"))

  def targetsWithImpl =
    targets.filter(t => t.tasks.exists(t => t.`type` == "implementation"))

  def targetsByToolchain(toolchain: String) =
    targets.filter(_.toolchain == toolchain)

  def taskNames = targets.flatMap(_.tasks).map(_.name)

  def taskByTaskTargetName(taskName: String, targetName: String): Option[(TargetConfig, TaskConfig)] = {
    val r = targets.filter(_.name == targetName)
    if (r.isEmpty) return None

    assert(r.length == 1)
    val target = r.head
    val tasks  = target.tasks.filter(_.name == taskName)
    if (tasks.isEmpty) return None

    assert(tasks.length == 1)
    Some(target, tasks.head)
  }

  /** Auto find target and task
    * @param taskName name of task
    * @return
    */
  def taskByName(taskName: String): Option[(TargetConfig, TaskConfig)] =
    targets
      .map(target => (target, target.tasks.find(_.name == taskName)))
      .filter(t => t._2.nonEmpty)
      .map(t => (t._1, t._2.get))
      .headOption

  def headTarget = targets.headOption

  def headTask = targets.headOption.flatMap(t => t.tasks.headOption)

  def headTargetTask =
    targets.headOption
      .map(target => (target, target.tasks.headOption))
      .filter(_._2.nonEmpty)
      .map(f => (f._1, f._2.get))
}

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"

  def getConfig(implicit project: ScaledaProject): Option[ProjectConfig] = {
    project.configFile.flatMap(p => {
      try {
        val config = YAMLHelper(new File(p), classOf[ProjectConfig])
        KernelLogger.debug(s"Loaded project config ${JSONHelper(config)}")
        // generate node relationship
        config.targets.foreach(target => {
          target.parentNode = Some(config)
          target.tasks.foreach(task => task.parentNode = Some(target))
        })
        Some(config)
      } catch {
        case e: com.fasterxml.jackson.databind.exc.MismatchedInputException =>
          KernelLogger.warn(s"Failed to parse project config file $p")
          None
      }
    })
  }

  def saveConfig(projectConfig: ProjectConfig, targetFile: File): Unit = {
    YAMLHelper(projectConfig, targetFile)
  }

  def libraryIpPaths: Set[File] = Set(Paths.getIpDir)

  def projectIpPaths(implicit project: ScaledaProject): Set[File] =
    Set(".ip", "ip", "ips")
      .map(p => new File(project.projectBase.get, p))
      .filter(_.exists())

  def libraryIps: Map[String, ProjectConfig] =
    libraryIpPaths.flatMap(p => KernelFileUtils.parseIpParentDirectory(p)).toMap

  def projectBasicIps(implicit
      manifest: ScaledaProject
  ): Map[String, ProjectConfig] =
    projectIpPaths.flatMap(p => KernelFileUtils.parseIpParentDirectory(p)).toMap
}

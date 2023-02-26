package top.criwits.scaleda
package kernel.project.config

import kernel.utils.KernelLogger
import kernel.utils.serialise.{JSONHelper, YAMLHelper}

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

import java.io.File

/** Case class for project-level config, i.e. `scaleda.yml`
  * @param name Project name
  * @param `type` Project type, should be 'rtl'
  * @param source Project source folder
  * @param test Project test folder
  * @param topModule Top module name, can be overriden by [[TargetConfig]] or [[TaskConfig]]
  * @param targets List of [[TargetConfig]]
  */
@JsonInclude(Include.NON_EMPTY)
case class ProjectConfig(
    name: String = "default-project",
    `type`: String = "rtl",
    source: String = "src/",
    test: String = "test/",
    topModule: Option[String] = None,
    targets: Array[TargetConfig] = Array()
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

  def taskByName(name: String): Option[(TargetConfig, TaskConfig)] = {
    val r = targets
      .map(task => (task, task.tasks))
      .filter(f => f._2.exists(t => t.name == name))
      .map(f =>
        (f._1, f._2.reduceOption((a, b) => if (a.name == name) a else b))
      )
    if (r.isEmpty) return None
    val h = r.head
    if (h._2.isEmpty) return None
    Some((h._1, h._2.get))
  }

  def headTarget = targets.headOption

  def headTask = targets.headOption.flatMap(t => t.tasks.headOption)
}

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"
  var projectBase: Option[String] = None
  var configFile: Option[String] = None

  def getConfig(path: Option[String] = configFile): Option[ProjectConfig] = {
    path match {
      case Some(p) =>
        val config = YAMLHelper(new File(p), classOf[ProjectConfig])
        KernelLogger.debug(s"Loaded project config ${JSONHelper(config)}")
        // generate node relationship
        config.targets.foreach(target => {
          target.parentNode = Some(config)
          target.tasks.foreach(task => task.parentNode = Some(target))
        })
        Some(config)
      case None => None
    }
  }

  def config = getConfig().get

  def headTarget = getConfig().flatMap(c => c.headTarget)

  def headTask = getConfig().flatMap(c => c.headTask)

  private def saveConfig(projectConfig: ProjectConfig): Unit = {
    YAMLHelper(projectConfig, new File(configFile.get))
  }

  def insertOrReplaceTarget(oldTargetName: String, target: TargetConfig): ProjectConfig = {
    ProjectConfig
      .getConfig()
      .map(c => {
        val projectConfig = if (c.targets.exists(_.name == oldTargetName)) {
          val newTargets = c.targets.filter(_.name != oldTargetName) :+ target
          c.copy(targets = newTargets)
        } else {
          c.copy(targets = c.targets :+ target)
        }
        saveConfig(projectConfig)
        projectConfig
      })
      .orNull
  }

  def insertOrReplaceTask(
      targetName: String,
      oldTaskName: String,
      task: TaskConfig
  ): ProjectConfig = {
    ProjectConfig
      .getConfig()
      .map(projectConfig => {
        if (!projectConfig.targets.exists(_.name == targetName)) {
          KernelLogger.error(
            s"Cannot apply task ${task.name}: no target named ${targetName}"
          )
          null
        } else {
          val target = projectConfig.targets.find(_.name == targetName).get
          val r = if (target.tasks.exists(_.name == oldTaskName)) {
            // replace exist task in target and replace target in config
            val newTasks = target.tasks.filter(_.name != oldTaskName) :+ task
            val newTarget = target.copy(tasks = newTasks)
            projectConfig.copy(targets =
              projectConfig.targets.map(t =>
                if (t.name == newTarget.name) newTarget else t
              )
            )
          } else {
            // append task to target, and replace target in config
            val targets = projectConfig.targets.map(t =>
              if (t.name == target.name)
                target.copy(tasks = target.tasks :+ task)
              else t
            )
            projectConfig.copy(targets = targets)
          }
          saveConfig(r)
          r
        }
      })
      .orNull
  }
}

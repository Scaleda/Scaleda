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
  * @param sources Project sources, items can be one file or directory
  * @param test Project test folder
  * @param topModule Top module name, can be overridden by [[TargetConfig]] or [[TaskConfig]]
  * @param targets List of [[TargetConfig]]
  */
@JsonInclude(Include.NON_EMPTY)
case class ProjectConfig(
    name: String = "default-project",
    `type`: String = "rtl",
    source: String = "src/",
    sources: Seq[String] = Seq(),
    test: String = "test/",
    topModule: Option[String] = None,
    constraints: Option[String] = None,
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
    targets.headOption.map(target => (target, target.tasks.headOption))
      .filter(_._2.nonEmpty)
      .map(f => (f._1, f._2.get))
}

object ProjectConfig {
  val defaultConfigFile = "scaleda.yml"

  // NOTICE: These two variables are global flag to indicated if a project is loaded
  // they should be always updated simultaneously
  var projectBase: Option[String] = None
  var configFile: Option[String]  = None

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

  def saveConfig(projectConfig: ProjectConfig): Unit = {
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
            val newTasks  = target.tasks.filter(_.name != oldTaskName) :+ task
            val newTarget = target.copy(tasks = newTasks)
            projectConfig.copy(targets = projectConfig.targets.map(t => if (t.name == newTarget.name) newTarget else t))
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

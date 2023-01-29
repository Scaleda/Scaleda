package top.criwits.scaleda
package kernel.toolchain

import kernel.project.config.{TaskConfig, TaskType}
import kernel.shell.command.CommandDeps
import kernel.toolchain.executor.Executor
import kernel.toolchain.impl._
import kernel.utils.serialise.YAMLHelper
import kernel.utils.{KernelLogger, Paths}

import java.io.{File, FilenameFilter}
import scala.collection.mutable.ListBuffer

/**
 * A [[Toolchain]] is an external software used to perform simulation, synthesis and implementation.
 * Applied toolchains, like [[Vivado]] or [[Quartus]], extend this abstract base class.
 *
 * @param executor An [[Executor]] used to hold information like configurations.
 */
abstract class Toolchain(val executor: Executor) {
  def getInternalID: String

  def getName: String

  def simulate(task: TaskConfig): Seq[CommandDeps] = ???

  def synthesise(task: TaskConfig): Seq[CommandDeps] = ???

  def implement(task: TaskConfig): Seq[CommandDeps] = ???

  def commands(task: TaskConfig) = task.taskType match {
    case TaskType.Simulation => simulate(task)
    case TaskType.Synthesis => synthesise(task)
    case TaskType.Implement => implement(task)
  }
}

object Toolchain {
  /**
   * All valid toolchains are listed here, with their corresponding implementation class
   */
  val toolchains: Map[String, (String, Executor => Toolchain, Set[TaskType.Value])] = Map(
    Vivado.internalID -> (Vivado.userFriendlyName, (executor: Executor) => new Vivado(executor), Vivado.supportedTask),
    Quartus.internalID -> (Quartus.userFriendlyName, (executor: Executor) => new Quartus(executor), Quartus.supportedTask),
    PDS.internalID -> (PDS.userFriendlyName, (executor: Executor) => new PDS(executor), PDS.supportedTask),
    Verilator.internalID -> (Verilator.userFriendlyName, (executor: Executor) => new Verilator(executor), Verilator.supportedTask),
    IVerilog.internalID -> (IVerilog.userFriendlyName, (executor: Executor) => new IVerilog(executor), IVerilog.supportedTask),
  )

  def toolchainIds = toolchains.keys.toArray

  def toolchainNames = toolchains.map(_._2._1).toArray

  /**
   * Profiles for different toolchains, loaded by [[ToolchainProfileLoader]]
   */

  private val defaultConfigDirectory: String = Paths.getToolchainsDir.getAbsolutePath

  private var cachedProfiles = new ListBuffer[ToolchainProfile]

  /**
   * Load toolchain profiles from given directory
   *
   * @param path Directory to save profiles
   */
  def profiles(path: String = defaultConfigDirectory, cache: Boolean = false): ListBuffer[ToolchainProfile] = {
    if (cache && cachedProfiles.nonEmpty) return cachedProfiles
    val profiles: ListBuffer[ToolchainProfile] = new ListBuffer[ToolchainProfile]

    val directory = new File(path)
    if (directory.isFile) throw new RuntimeException(s"${directory.getAbsolutePath} is a file")
    if (!directory.exists()) directory.mkdirs()

    val files = directory.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".yml")
    })
    files.foreach(f => {
      val newItem = YAMLHelper(f, classOf[ToolchainProfile])
      newItem.file = Some(f)
      profiles += newItem
    })
    KernelLogger.debug(s"${profiles.size} profiles loaded")
    cachedProfiles = profiles
    cachedProfiles
  }

  def syncProfile(profile: ToolchainProfile): Unit =
    profile.file match {
      case Some(f) => if (profile.removed) removeProfile(profile) else YAMLHelper(profile, f)
      case None =>
        val shortname = profile.profileName.toLowerCase().replaceAll("[^a-z0-9]+", "-")
        YAMLHelper(profile, new File(defaultConfigDirectory, shortname + ".yml"))
    }

  def removeProfile(profile: ToolchainProfile): Boolean =
    profile.file match {
      case Some(f) => f.delete()
      case None => false
    }
}
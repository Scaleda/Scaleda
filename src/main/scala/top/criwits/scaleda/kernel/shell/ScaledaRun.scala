package top.criwits.scaleda
package kernel.shell

import idea.runner.ScaledaRuntimeInfo
import kernel.project.config.{TargetConfig, TaskConfig, TaskType}
import kernel.shell.command.{CommandDeps, CommandRunner, RemoteCommandDeps}
import kernel.toolchain.executor.{Executor, ImplementExecutor, SimulationExecutor, SynthesisExecutor}
import kernel.toolchain.{Toolchain, ToolchainProfile}
import kernel.utils.KernelLogger

import java.io.File
import scala.collection.mutable.ArrayBuffer

object ScaledaRun {

  /** Run a task
    * @param handler A [[ScaledaRunHandler]] used to redirect output and error
    * @param workingDir Working directory
    * @param target A [[TargetConfig]]
    * @param task A [[TaskConfig]]
    * @param profile An optional [[ToolchainProfile]]
    */
  private def runTask(
      handler: ScaledaRunHandler,
      rt: ScaledaRuntimeInfo,
      remoteDeps: Option[RemoteCommandDeps] = None
  ): Unit = {
    require(rt.profile.profileName.nonEmpty, "must provide profile before runTask")
    val remoteDepsTmp = remoteDeps.getOrElse(RemoteCommandDeps(host = rt.profile.host))
    val remoteDepsUse = if (remoteDepsTmp.host.nonEmpty) Some(remoteDepsTmp) else None
    KernelLogger.info(s"runTask workingDir=${rt.workingDir.getAbsoluteFile}")

    val info = Toolchain.toolchains(rt.target.toolchain)
    val rtProcessed =
      if (rt.task.preset) {
        Toolchain.toolchainPresetHandler.get(rt.target.toolchain).map(_.handlePreset(rt)).getOrElse(rt)
      } else rt
    val toolchain = info._2(rt.executor)
    val commands  = toolchain.commands(rtProcessed.task)
    CommandRunner.executeLocalOrRemote(remoteDepsUse, commands, handler)
  }

  def runTaskBackground(
      handler: ScaledaRunHandler,
      runtime: ScaledaRuntimeInfo,
      daemon: Boolean = true
  ): Thread = {
    val t = new Thread(() => runTask(handler, runtime))
    t.setDaemon(daemon)
    t
  }

  def generateExecutor(
      target: TargetConfig,
      task: TaskConfig,
      profile: ToolchainProfile,
      workingDir: File
  ): Executor = {
    val workingDirName = target.name + "-" + task.name
    task.taskType match {
      case TaskType.Simulation =>
        // FIXME: GENERATE TESTBENCH?
        val testbench    = task.findTopModule.get // FIXME: should not get if None, but...
        val workingPlace = new File(new File(workingDir, ".sim"), workingDirName)
        SimulationExecutor(
          workingDir = workingPlace,
          topModule = testbench,
          vcdFile = new File(workingPlace, testbench + "_waveform.vcd"),
          profile = profile
        )
      case TaskType.Synthesis =>
        SynthesisExecutor(
          workingDir = new File(new File(workingDir, ".synth"), workingDirName),
          topModule = task.findTopModule.get,
          profile = profile
        )
      case TaskType.Implement =>
        ImplementExecutor(
          workingDir = new File(new File(workingDir, ".impl"), workingDirName),
          topModule = task.findTopModule.get,
          profile = profile
        )
    }
  }
}

trait ScaledaRunHandler {
  def onStdout(data: String): Unit

  def onStderr(data: String): Unit

  /** Invoked when one return value captured, and [[finishedAll]] == true after all commands done
    * @param returnValue return value
    * @param finishedAll is all commands finished
    */
  def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit

  /** Return true if handler is stopping this process
    * @return terminating
    */
  def isTerminating: Boolean = false

  /** Invoked before every command call
    * @param command command
    */
  def onShellCommand(command: CommandDeps): Unit = {}

  def onStepDescription(data: String): Unit = {}

  def expectedReturnValue: Int = 0
}

/** RunHandler that logging outputs to [[KernelLogger]]
  */
trait ScaledaRunKernelHandlerWithReturn extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = KernelLogger.info(data)

  override def onStderr(data: String): Unit = KernelLogger.error(data)
}

object ScaledaRunKernelHandler extends ScaledaRunKernelHandlerWithReturn {
  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
    // TODO: i18n
    KernelLogger.info(s"command done, returns $returnValue, finishedAll: $finishedAll, meetErrors: $meetErrors")
  }
}

object ScaledaRunKernelRemoteHandler extends ScaledaRunKernelHandlerWithReturn {
  override def onStdout(data: String): Unit = KernelLogger.info("[remote]", data)

  override def onStderr(data: String): Unit = KernelLogger.error("[remote]", data)

  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
    // TODO: i18n
    if (meetErrors) {
      KernelLogger.error("[remote]", s"command error, returns $returnValue")
    } else {
      KernelLogger.info("[remote]", s"command done, returns $returnValue")
    }
  }
}

/** RunHandler that records return value and outputs to [[returnValues]]
  * @param returnValues optional array to store results
  * @param outputs array to store stdout outputs
  * @param errors array to store stderr outputs
  */
class ScaledaRunHandlerToArray(
    returnValues: Option[ArrayBuffer[Int]],
    outputs: ArrayBuffer[String],
    errors: Option[ArrayBuffer[String]] = None
) extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = outputs.addOne(data)

  override def onStderr(data: String): Unit =
    errors.map(_.addOne(data)).getOrElse(onStdout(data))

  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit =
    returnValues.foreach(_.addOne(returnValue))
}

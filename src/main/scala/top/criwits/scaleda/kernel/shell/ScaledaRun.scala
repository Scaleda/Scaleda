package top.criwits.scaleda
package kernel.shell

import kernel.project.config.{TargetConfig, TaskConfig, TaskType}
import kernel.shell.command.{CommandDeps, CommandRunner, RemoteCommandDeps}
import kernel.toolchain.executor.{Executor, ImplementExecutor, SimulationExecutor, SynthesisExecutor}
import kernel.toolchain.impl.{IVerilog, Vivado}
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
  def runTask(
      handler: ScaledaRunHandler,
      workingDir: File,
      target: TargetConfig,
      task: TaskConfig,
      profile: Option[ToolchainProfile] = None,
      remoteDeps: Option[RemoteCommandDeps] = None
  ): Unit = {
    val remoteDepsTmp = remoteDeps.getOrElse(RemoteCommandDeps(host = profile.map(_.host).getOrElse("")))
    val remoteDepsUse = if (remoteDepsTmp.host.isEmpty) Some(remoteDepsTmp) else None
    KernelLogger.info(s"runTask workingDir=${workingDir.getAbsoluteFile}")

    val info = Toolchain.toolchains(target.toolchain)
    // find profile
    var profileUse = profile
    if (profileUse.isEmpty) {
      profileUse = Toolchain
        .profiles()
        .find(p => p.toolchainType == target.toolchain)
    }
    profileUse
      .map(profile => {
        // generate executor
        val workingDirName = target.name + "-" + task.name
        val executor = task.taskType match {
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
        // FIXME: Tricky?
        handler.executor = executor
        val taskUse =
          if (task.preset) {
            target.toolchain match {
              case Vivado.internalID =>
                val r = new Vivado.TemplateRenderer(
                  executor = executor,
                  targetConfig = target,
                  taskConfig = task
                )
                r.render()
                task.copy(tcl = Some(task.taskType match {
                  case TaskType.Simulation => "run_sim.tcl"
                  case TaskType.Synthesis => "run_synth.tcl"
                  case TaskType.Implement => "run_impl.tcl"
                  case TaskType.Programming => "run_program.tcl"
                }))
              case IVerilog.internalID =>
                task
              case _ =>
                KernelLogger.error(s"not supported preset: ${target.toolchain}")
                task
            }
          } else task
        val toolchain = info._2(executor)
        val commands  = toolchain.commands(taskUse)
        CommandRunner.executeLocalOrRemote(remoteDepsUse, commands, handler)
      })
      .getOrElse(KernelLogger.error("No profile found!"))
  }

  def runTaskBackground(
      handler: ScaledaRunHandler,
      workingDir: File,
      target: TargetConfig,
      task: TaskConfig,
      daemon: Boolean = true,
      profile: Option[ToolchainProfile] = None,
  ): Thread = {
    val t = new Thread(() => runTask(handler, workingDir, target, task, profile = profile))
    t.setDaemon(daemon)
    t
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

  // FIXME: What's for
  var executor: Executor = _
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

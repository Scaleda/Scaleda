package top.criwits.scaleda
package kernel.shell

import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig, TaskType}
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.toolchain.Toolchain
import kernel.toolchain.executor.{ImplementExecutor, SimulationExecutor, SynthesisExecutor}
import kernel.toolchain.impl.Vivado
import kernel.utils.KernelLogger

import java.io.File
import scala.collection.mutable.ArrayBuffer

object ScaledaRun {
  /**
   * Run a task
   * @param handler A [[ScaledaRunHandler]] used to redirect output and error
   * @param workingDir Working directory
   * @param target A [[TargetConfig]]
   * @param task A [[TaskConfig]]
   */
  def runTask(
      handler: ScaledaRunHandler,
      workingDir: File,
      target: TargetConfig,
      task: TaskConfig
  ) = {
    KernelLogger.info(s"runTask workingDir=${workingDir.getAbsoluteFile}")

      val info = Toolchain.toolchains(target.toolchain)
      // find profile
      Toolchain
        .profiles()
        .filter(p => p.toolchainType == target.toolchain)
        .foreach(profile => {
          // generate executor
          val executor = task.taskType match {
            case TaskType.Simulation =>
              SimulationExecutor(
                workingDir = new File(new File(workingDir, ".sim"), task.name),
                topModule = task.findTopModule.get, // FIXME
                profile = profile
              )
            case TaskType.Synthesis =>
              SynthesisExecutor(
                workingDir = new File(new File(workingDir, ".synth"), task.name),
                topModule = task.findTopModule.get,
                profile = profile
              )
            case TaskType.Implement =>
              ImplementExecutor(
                workingDir = new File(new File(workingDir, ".impl"), task.name),
                topModule = task.findTopModule.get,
                profile = profile
              )
          }
          val taskUse =
            if (task.preset) {
              target.toolchain match {
                case Vivado.internalID =>
                  val r = new Vivado.TemplateRenderer(
                    executor = executor,
                    targetConfig = target,
                    taskConfig = task,
                  )
                  r.render()
                  task.copy(tcl = Some(task.taskType match {
                    case TaskType.Implement => "run_impl.tcl"
                    case _ => "run_synth.tcl"
                  }))
                case _ =>
                  KernelLogger.error(s"not supported preset: ${target.toolchain}")
                  task
              }
            } else task
          val toolchain = info._2(executor)
          val commands = toolchain.commands(taskUse)
          CommandRunner.execute(commands, handler)
        })
  }

  def runTaskBackground(
      handler: ScaledaRunHandler,
      workingDir: File,
      target: TargetConfig,
      task: TaskConfig,
      daemon: Boolean = true
  ): Thread = {
    val t = new Thread(() => runTask(handler, workingDir, target, task))
    t.setDaemon(daemon)
    t.start()
    t
  }
}

trait ScaledaRunHandler {
  def onStdout(data: String): Unit

  def onStderr(data: String): Unit

  def onReturn(returnValue: Int): Unit

  def isTerminating: Boolean = false

  def onShellCommand(command: CommandDeps): Unit = {}

  def expectedReturnValue: Int = 0
}

trait ScaledaRunKernelHandlerWithReturn extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = KernelLogger.info(data)

  override def onStderr(data: String): Unit = KernelLogger.error(data)
}

object ScaledaRunKernelHandler extends ScaledaRunKernelHandlerWithReturn {
  override def onStdout(data: String): Unit = KernelLogger.info(data)

  override def onStderr(data: String): Unit = KernelLogger.error(data)

  override def onReturn(returnValue: Int): Unit =
    KernelLogger.info(s"command done, returns $returnValue")
}

class ScaledaRunHandlerToArray
(returnValues: Option[ArrayBuffer[Int]], outputs: ArrayBuffer[String], errors: Option[ArrayBuffer[String]] = None)
  extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = outputs.addOne(data)

  override def onStderr(data: String): Unit = errors.map(_.addOne(data)).getOrElse(onStdout(data))

  override def onReturn(returnValue: Int): Unit = returnValues.foreach(_.addOne(returnValue))
}
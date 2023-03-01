package top.criwits.scaleda
package idea.runner

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.shell.ScaledaRunHandler
import kernel.utils.BasicLogger

import com.intellij.execution.process.ProcessHandler
import top.criwits.scaleda.kernel.project.config.TaskConfig
import top.criwits.scaleda.kernel.shell.command.CommandDeps
import top.criwits.scaleda.kernel.toolchain.executor.Executor

import java.io.OutputStream

class ScaledaRunProcessHandler(logger: BasicLogger, task: TaskConfig, invokeAfterFinish: (TaskConfig, Executor) => Unit = (_: TaskConfig, _: Executor) => {})
    extends ProcessHandler
    with ScaledaRunHandler {
  var terminated = false
  var terminating = false

  // val logger = new ConsoleLogger(console)

  override def destroyProcessImpl(): Unit = {
    MainLogger.warn(
      s"destroyProcessImpl, running: ${terminated}, stopping: ${terminating}"
    )
    terminating = true
    notifyProcessTerminated(ret)
  }

  override def detachProcessImpl(): Unit = {
    MainLogger.warn("detachProcessImpl")
    notifyProcessDetached()
  }

  override def detachIsDefault(): Boolean = {
    // MainLogger.warn("detachIsDefault", false)
    false
  }

  override def isProcessTerminated: Boolean = {
    // MainLogger.warn("isProcessTerminated", terminated)
    terminated
  }

  override def isProcessTerminating: Boolean = {
    // MainLogger.warn("isProcessTerminating", terminating)
    terminating
  }

  private val outputStream = new OutputStream {
    override def write(i: Int): Unit = MainLogger.warn("getProcessInput:", i)
  }
  override def getProcessInput: OutputStream = {
    // new OutputStream {
    //   override def write(i: Int) = MainLogger.warn("getProcessInput:", i)
    // }
    // streamOutput
    // null
    outputStream
  }

  override def onShellCommand(command: CommandDeps) = logger.debug("cd", s"\"${command.path}\"", "&&", command.commands.mkString(" "))

  override def onStepDescription(data: String): Unit = logger.debug(data)

  override def onStdout(data: String): Unit = logger.info(data)

  override def onStderr(data: String): Unit = logger.warn(data)

  var ret: Int = 0

  override def onReturn(returnValue: Int, finishedAll: Boolean): Unit = {
    logger.info(ScaledaBundle.message("task.run.return.text", returnValue))
    ret = returnValue
    if (finishedAll) {
      terminating = false
      terminated = true
      // invoke
      invokeAfterFinish(task, executor)
    }
  }

  override def isTerminating: Boolean = terminating
}

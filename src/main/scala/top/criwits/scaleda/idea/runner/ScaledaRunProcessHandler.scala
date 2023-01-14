package top.criwits.scaleda
package idea.runner

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.shell.ScaledaRunHandler
import kernel.utils.BasicLogger

import com.intellij.execution.process.ProcessHandler
import top.criwits.scaleda.kernel.shell.command.CommandDeps

import java.io.OutputStream

class ScaledaRunProcessHandler(logger: BasicLogger)
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
  }

  override def detachProcessImpl(): Unit = {
    MainLogger.warn("detachProcessImpl")
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

  override def onShellCommand(command: CommandDeps) = logger.info("in", command.path, "executing", command.command)

  override def onStdout(data: String): Unit = logger.info(data)

  override def onStderr(data: String): Unit = logger.warn(data)

  override def onReturn(returnValue: Int): Unit = {
    logger.info(ScaledaBundle.message("task.run.return.text", returnValue))
    terminating = false
    terminated = true
  }

  override def isTerminating: Boolean = terminating
}

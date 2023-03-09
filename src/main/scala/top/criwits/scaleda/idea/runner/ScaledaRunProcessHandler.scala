package top.criwits.scaleda
package idea.runner

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.shell.ScaledaRunHandler
import kernel.shell.command.CommandDeps
import kernel.toolchain.executor.Executor
import kernel.utils.BasicLogger

import com.intellij.execution.process.ProcessHandler

import java.io.OutputStream

/** Handle a task process locally or remotely
  * @param logger using logger
  * @param task task
  * @param invokeAfterFinish will emit with [[task]] and [[Executor]] after `finishedAll`
  */
class ScaledaRunProcessHandler(
    logger: BasicLogger,
    rt: ScaledaRuntimeInfo,
    invokeAfterFinish: ScaledaRuntimeInfo => Unit = rt => {}
) extends ProcessHandler
    with ScaledaRunHandler {
  // Set terminating <- `true` to invoke stopping
  var terminating = false
  // terminated will be set `true` after process really terminated
  var terminated = false

  /** Called when destroy button clicked
    */
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

  override def detachIsDefault(): Boolean = false

  override def isProcessTerminated: Boolean = terminated

  override def isProcessTerminating: Boolean = terminating

  // Not in use
  private val outputStream = new OutputStream {
    override def write(i: Int): Unit = MainLogger.warn("getProcessInput:", i)
  }
  override def getProcessInput: OutputStream = outputStream

  override def onShellCommand(command: CommandDeps) = {
    // FIXME: cd {workdir} && {command} may not available when using `commands`. Delete this or fix it
    // logger.debug("cd", s"\"${command.path}\"", "&&", command.commands.mkString(" "))
  }

  override def onStepDescription(data: String): Unit = logger.debug(data)

  override def onStdout(data: String): Unit = logger.info(data)

  override def onStderr(data: String): Unit = logger.warn(data)

  var ret: Int = 0

  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
    val msg = ScaledaBundle.message("task.run.return.text", returnValue)
    if (meetErrors) logger.warn(msg)
    else logger.info(msg)
    ret = returnValue
    if (finishedAll || meetErrors) {
      terminating = false
      terminated = true
      // invoke only success all
      if (!meetErrors) invokeAfterFinish(rt)
    }
  }

  override def isTerminating: Boolean = terminating
}

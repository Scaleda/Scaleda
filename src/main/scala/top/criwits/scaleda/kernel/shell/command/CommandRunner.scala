package top.criwits.scaleda
package kernel.shell.command

import kernel.shell.ScaledaRunHandler
import kernel.utils.KernelLogger

import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import scala.concurrent.{Future, Promise}
import scala.sys.process._

case class CommandOutputStream(
    returnValue: Future[Int],
    stdOut: LinkedBlockingQueue[String],
    stdErr: LinkedBlockingQueue[String]
)

case class CommandDeps(
    command: String,
    path: String = "",
    envs: Seq[(String, String)] = Seq()
)

class CommandRunner(deps: CommandDeps) extends AbstractCommandRunner {
  val path: String =
    if (deps.path.isEmpty) System.getProperty("user.dir") else deps.path
  val command: String = deps.command
  val envs: Seq[(String, String)] = deps.envs
  val workingDir = new File(path)
  if (!workingDir.exists()) workingDir.mkdirs()
  private val procBuilder = Process(command, workingDir, envs: _*)
  private var process: Option[Process] = None
  private var terminate: Boolean = false
  private var terminating: Boolean = false
  private var terminated: Boolean = false
  protected val returnValue = Promise[Int]()
  protected val stdOut = new LinkedBlockingQueue[String]
  protected val stdErr = new LinkedBlockingQueue[String]
  private val delay = 100
  val thread = new Thread(() => {
    try {
      process = Some(procBuilder.run(ProcessLogger(
        out => stdOut.put(out),
        err => stdErr.put(err)
      )))
      while (process.get.isAlive()) {
        if (terminate && !terminating) {
          terminating = true
          process.get.destroy()
        }
        Thread.sleep(delay)
      }
      if (terminate) terminate = false
      if (terminating) terminating = false
      terminated = true
      returnValue.success(process.get.exitValue())
    } catch {
      case e: Throwable => stdErr.put(e.toString)
        throw e
    } finally {
      if (terminate) terminate = false
      if (terminating) terminating = false
      terminated = true
      if (!returnValue.isCompleted) returnValue.success(-1)
    }
  })

  override def run: CommandOutputStream = {
    thread.setDaemon(true)
    // thread.setDaemon(false)
    thread.start()
    CommandOutputStream(returnValue.future, stdOut, stdErr)
  }

  override def doTerminate() = terminate = true

  override def canTerminate = true

  override def isTerminating = terminate || terminating

  override def isTerminated = terminated
}

//noinspection DuplicatedCode
object CommandRunner {
  private val delay = 50

  private def flushStream(
      stream: LinkedBlockingQueue[String],
      handler: String => Unit
  ): Unit = {
    try {
      while (stream.size() > 0) {
        handler(stream.take())
      }
    } catch {
      case _: InterruptedException => {}
    }
  }

  def executeLocalOrRemote(
      remoteCommandDeps: Option[RemoteCommandDeps],
      commands: Seq[CommandDeps],
      handler: ScaledaRunHandler,
      ignoreErrors: Boolean
  ): Unit = {
    var meetErrors = false
    commands.foreach(command => {
      if (!ignoreErrors && meetErrors) return
      KernelLogger.info(s"running command: ${command.command}")
      handler.onShellCommand(command)
      val runner = remoteCommandDeps
        .map(r => new RemoteCommandRunner(command, r))
        .getOrElse(new CommandRunner(command))
      val r = runner.run
      do {
        flushStream(r.stdOut, handler.onStdout)
        flushStream(r.stdErr, handler.onStderr)
        Thread.sleep(delay)
      } while (!r.returnValue.isCompleted && !handler.isTerminating)
      if (handler.isTerminating) {
        runner.doTerminate()
        while (runner.isTerminating) Thread.sleep(delay)
        while (!r.returnValue.isCompleted) Thread.sleep(delay)
      }
      // To ensure output & error are got for the last time
      r.stdOut.forEach(s => handler.onStdout(s))
      r.stdErr.forEach(s => handler.onStderr(s))
      val returnValue = r.returnValue.value.get.get
      handler.onReturn(returnValue)
      if (returnValue != handler.expectedReturnValue) meetErrors = true
    })
  }

  def execute(commands: Seq[CommandDeps], handler: ScaledaRunHandler, ignoreErrors: Boolean = false): Unit =
    executeLocalOrRemote(None, commands, handler, ignoreErrors)
}

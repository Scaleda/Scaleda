package top.criwits.scaleda
package kernel.shell.command

import kernel.utils.KernelLogger

import top.criwits.scaleda.kernel.shell.ScaledaRunHandler

import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import scala.concurrent.{Future, Promise}
import scala.sys.process._

case class CommandOutputStream(returnValue: Future[Int],
                               stdOut: LinkedBlockingQueue[String],
                               stdErr: LinkedBlockingQueue[String])

case class CommandDeps
(command: String, path: String = "", envs: Seq[(String, String)] = Seq())

class CommandRunner(deps: CommandDeps) extends AbstractCommandRunner {
  val path = if (deps.path.isEmpty) System.getProperty("user.dir") else deps.path
  val command = deps.command
  val envs = deps.envs
  val workingDir = new File(path)
  private val proc = Process(command, workingDir, envs: _*)
  protected val returnValue = Promise[Int]()
  protected val stdOut = new LinkedBlockingQueue[String]
  protected val stdErr = new LinkedBlockingQueue[String]
  private val thread = new Thread(() => {
    val exitValue = proc ! ProcessLogger(
      out => stdOut.put(out),
      err => stdErr.put(err),
    )
    returnValue.success(exitValue)
  })

  override def run: CommandOutputStream = {
    thread.setDaemon(true)
    // thread.setDaemon(false)
    thread.start()
    CommandOutputStream(returnValue.future, stdOut, stdErr)
  }
}

object CommandResponse extends Enumeration {
  val Stdout, Stderr, Return = Value
}

//noinspection duplicatedcode
object CommandRunner {
  val delay = 300

  def executeLocalOrRemote(remoteCommandDeps: Option[RemoteCommandDeps], commands: Seq[CommandDeps], handler: ScaledaRunHandler): Unit =
    commands.foreach(command => {
      KernelLogger.info(s"running command: ${command.command}")
      val runner = remoteCommandDeps.map(r => new RemoteCommandRunner(command, r).run)
        .getOrElse(new CommandRunner(command).run)
      do {
        runner.stdOut.forEach(s => handler.onStdout(s))
        runner.stdErr.forEach(s => handler.onStderr(s))
        Thread.sleep(delay)
      } while (!runner.returnValue.isCompleted)
      // To ensure output & error are got for the last time
      runner.stdOut.forEach(s => handler.onStdout(s))
      runner.stdErr.forEach(s => handler.onStderr(s))
      handler.onReturn(runner.returnValue.value.get.get)
    })

  def execute(commands: Seq[CommandDeps], handler: ScaledaRunHandler): Unit =
    executeLocalOrRemote(None, commands, handler)
}

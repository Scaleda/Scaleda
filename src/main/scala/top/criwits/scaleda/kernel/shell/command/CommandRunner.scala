package top.criwits.scaleda
package kernel.shell.command

import kernel.utils.KernelLogger

import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import scala.concurrent.{Future, Promise}
import scala.sys.process._

case class CommandOutputStream(returnValue: Future[Int], stdOut: LinkedBlockingQueue[String], stdErr: LinkedBlockingQueue[String])

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
    thread.start()
    CommandOutputStream(returnValue.future, stdOut, stdErr)
  }
}

object CommandResponse extends Enumeration {
  val Stdout, Stderr, Return = Value
}

object CommandRunner {
  val delay = 300

  def execute(commands: Seq[CommandDeps], callback: (CommandResponse.Value, Any) => Unit): Unit =
    commands.foreach(command => {
      val runner = new CommandRunner(command).run
      while (!runner.returnValue.isCompleted) {
        runner.stdOut.forEach(s => callback(CommandResponse.Stdout, s))
        runner.stdErr.forEach(s => callback(CommandResponse.Stderr, s))
        Thread.sleep(delay)
      }
      callback(CommandResponse.Return, runner.returnValue.value.get.get)
    })
}

object CommandRunnerTest extends App {
  val runner = new CommandRunner(CommandDeps("ping -c 3 127.0.0.1"))
  val r = runner.run
  while (!r.returnValue.isCompleted) {
    r.stdOut.forEach(s => KernelLogger.info(s))
    r.stdErr.forEach(s => KernelLogger.error(s))
    Thread.sleep(CommandRunner.delay)
  }
  KernelLogger.info(s"return value: ${r.returnValue.value.get}")
}
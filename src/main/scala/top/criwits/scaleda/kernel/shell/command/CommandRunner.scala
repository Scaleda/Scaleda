package top.criwits.scaleda
package kernel.shell.command

import kernel.shell.ScaledaRunHandler
import kernel.utils.KernelLogger

import java.io.File
import java.util.Scanner
import java.util.concurrent.LinkedBlockingQueue
import scala.async.Async.async
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.jdk.javaapi.CollectionConverters

case class CommandOutputStream(
    returnValue: Future[Int],
    stdOut: LinkedBlockingQueue[String],
    stdErr: LinkedBlockingQueue[String]
)

case class CommandDeps(
    args: Seq[String] = Seq(),
    path: String = "",
    envs: Seq[(String, String)] = Seq(),
    description: String = ""
)

class CommandRunner(deps: CommandDeps) extends AbstractCommandRunner {
  val path: String =
    if (deps.path.isEmpty) System.getProperty("user.dir") else deps.path
  val args                        = deps.args
  val envs: Seq[(String, String)] = deps.envs
  val workingDir                  = new File(path)
  if (!workingDir.exists()) workingDir.mkdirs()
  KernelLogger.info("exec deps:", deps)
  private val procBuilder = new ProcessBuilder()
  procBuilder.command(CollectionConverters.asJava(args))
  procBuilder.directory(workingDir)
  deps.envs.foreach(d => procBuilder.environment().put(d._1, d._2))
  private var process: Option[Process] = None
  private var terminate: Boolean       = false
  private var terminating: Boolean     = false
  private var terminated: Boolean      = false
  protected val returnValue            = Promise[Int]()
  protected val stdOut                 = new LinkedBlockingQueue[String]
  protected val stdErr                 = new LinkedBlockingQueue[String]
  private val delay                    = 100
  val thread = new Thread(() => {
    try {
      process = Some(procBuilder.start())
      val out        = process.get.getInputStream
      val err        = process.get.getErrorStream
      val outScanner = new Scanner(out)
      val errScanner = new Scanner(err)
      val scannerThread = new Thread(() => {
        while (outScanner.hasNextLine) {
          stdOut.put(outScanner.nextLine())
        }
        while (errScanner.hasNextLine) {
          stdErr.put(errScanner.nextLine())
        }
      })
      scannerThread.setName("command-runner-output-scanner")
      scannerThread.start()
      while (process.get.isAlive) {
        if (terminate && !terminating) {
          terminating = true
          process.get.destroy()
          // process.get.destroyForcibly()
        }
        Thread.sleep(delay)
      }
      if (terminate) terminate = false
      if (terminating) terminating = false
      terminated = true
      scannerThread.interrupt()
      returnValue.success(process.get.exitValue())
    } catch {
      case e: Throwable =>
        stdErr.put(e.toString)
        throw e
    } finally {
      if (terminate) terminate = false
      if (terminating) terminating = false
      terminated = true
      if (!returnValue.isCompleted) returnValue.success(-1)
    }
  })
  thread.setName(s"command-runner-${args.headOption.getOrElse("unknown")}")

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
      handler: String => Unit,
      extraOutput: Boolean = false
  ): Unit = {
    try {
      while (stream.size() > 0) {
        val v = stream.take()
        handler(v)
        if (extraOutput) KernelLogger.debug("[local executor]", v)
      }
    } catch {
      case _: InterruptedException => {}
    }
  }

  def executeLocalOrRemote(
      remoteDeps: Option[RemoteCommandDeps],
      commands: Seq[CommandDeps],
      handler: ScaledaRunHandler,
      ignoreErrors: Boolean = false
  ): Unit = {
    var meetErrors = false
    var step       = 1
    val steps      = commands.length
    commands.foreach(command => {
      if (!ignoreErrors && meetErrors) return
      KernelLogger.info(s"running command: ${command.args.map(s => s"\"$s\" ").mkString("")}")
      val stepHint = (s"-- Step ($step/$steps)") + (
        if (command.description != "") ": " + command.description
        else ""
      )
      handler.onStepDescription(stepHint)
      handler.onShellCommand(command)
      val isRemote = remoteDeps.nonEmpty
      if (isRemote) KernelLogger.info("will execute remotely:", remoteDeps.get)
      val runner = remoteDeps
        .map(r => new RemoteCommandRunner(command, r))
        .getOrElse(new CommandRunner(command))
      val r = runner.run
      do {
        flushStream(r.stdOut, handler.onStdout, extraOutput = !isRemote)
        flushStream(r.stdErr, handler.onStderr, extraOutput = !isRemote)
        Thread.sleep(delay)
      } while (!r.returnValue.isCompleted && !handler.isTerminating)
      if (handler.isTerminating) {
        runner.doTerminate()
        while (runner.isTerminating) Thread.sleep(delay)
        while (!r.returnValue.isCompleted) Thread.sleep(delay)
      }
      // To ensure output & error are got for the last time
      flushStream(r.stdOut, handler.onStdout, extraOutput = !isRemote)
      flushStream(r.stdErr, handler.onStderr, extraOutput = !isRemote)
      val returnValue = r.returnValue.value.get.get
      if (returnValue != handler.expectedReturnValue) meetErrors = true
      handler.onReturn(returnValue, commands.last == command, meetErrors)
      step += 1
    })
  }

  def execute(commands: Seq[CommandDeps], handler: ScaledaRunHandler, ignoreErrors: Boolean = false): Unit =
    executeLocalOrRemote(None, commands, handler, ignoreErrors)

  def executeAsyncLocalOrRemote(
      remoteCommandDeps: Option[RemoteCommandDeps],
      commands: Seq[CommandDeps],
      handler: ScaledaRunHandler,
      ignoreErrors: Boolean = false
  ): Future[Seq[Int]] = async {
    val returnValues = ArrayBuffer[Int]()
    val returnValueHandler = new ScaledaRunHandler {
      override def onStdout(data: String): Unit = handler.onStdout(data)

      override def onStderr(data: String): Unit = handler.onStderr(data)

      override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
        handler.onReturn(returnValue, finishedAll, meetErrors)
        returnValues.addOne(returnValue)
      }

      override def isTerminating: Boolean = handler.isTerminating

      override def onShellCommand(command: CommandDeps): Unit = handler.onShellCommand(command)

      override def expectedReturnValue: Int = handler.expectedReturnValue
    }
    executeLocalOrRemote(remoteCommandDeps, commands, returnValueHandler, ignoreErrors)
    returnValues.toSeq
  }

  def executeAsync(
      commands: Seq[CommandDeps],
      handler: ScaledaRunHandler,
      ignoreErrors: Boolean = false
  ): Future[Seq[Int]] =
    executeAsyncLocalOrRemote(None, commands, handler, ignoreErrors)
}

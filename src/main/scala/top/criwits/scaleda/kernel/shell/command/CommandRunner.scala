package top.criwits.scaleda
package kernel.shell.command

import top.criwits.scaleda.kernel.utils.KernelLogger

import java.io.File
import java.util.stream.Stream
import scala.concurrent.{Future, Promise}
import scala.sys.process._

case class CommandOutputStream(returnValue: Future[Int], stdOut: Stream[String], stdErr: Stream[String])

class CommandRunner(command: String, private var path: String = null, env: Seq[(String, String)] = Seq()) {
  if (path == null) path = System.getProperty("user.dir")
  val workingDir = new File(path)
  private val proc = Process(command, workingDir, env: _*)
  private val returnValue = Promise[Int]
  private val stdOut = new java.util.LinkedList[String]
  private val stdErr = new java.util.LinkedList[String]
  private val thread = new Thread(() => {
    val exitValue = proc ! ProcessLogger(
      out => stdOut.push(out),
      err => stdErr.push(err),
    )
    returnValue.success(exitValue)
  })

  def run: CommandOutputStream = {
    thread.setDaemon(true)
    thread.start()
    CommandOutputStream(returnValue.future, stdOut.stream(), stdErr.stream())
  }
}

object CommandRunnerTest extends App {
  val runner = new CommandRunner("ping -c 3 127.0.0.1")
  val r = runner.run
  while (!r.returnValue.isCompleted) {
    r.stdOut.forEach(s => KernelLogger.info(s))
    r.stdErr.forEach(s => KernelLogger.error(s))
    Thread.sleep(300)
  }
  KernelLogger.info(s"return value: ${r.returnValue.value.get}")
}
package top.criwits.scaleda
package kernel.shell.command

import kernel.net.fuse.fs.FuseTransferMessage
import kernel.net.fuse.{FuseDataProvider, FuseTransferClient}
import kernel.net.remote.RunReplyType._
import kernel.net.remote.{RunRequest, StringTriple}
import kernel.net.{RemoteClient, RemoteServer}
import kernel.shell.ScaledaRunHandler
import kernel.utils.KernelLogger

import com.google.protobuf.ByteString

import java.io.File
import scala.language.existentials

case class RemoteCommandDeps(
    projectRoot: File,
    host: String = "localhost",
    port: Int = RemoteServer.DEFAULT_PORT
)

class RemoteCommandRunner(
    deps: CommandDeps,
    remoteCommandDeps: RemoteCommandDeps
) extends CommandRunner(deps)
    with AbstractCommandRunner {
  val request = new RunRequest(
    commands = deps.args,
    path = deps.path,
    envs = deps.envs.map(t => new StringTriple(t._1, t._2)),
    projectBase = remoteCommandDeps.projectRoot.getAbsolutePath
  )
  override val thread = new Thread(() => {
    val fuseStartWaits = new Object
    var fuseStarted    = false
    val shellThread = new Thread(() => {
      // wait until fuse started
      fuseStartWaits.synchronized {
        fuseStartWaits.wait(10000)
      }
      if (!fuseStarted) throw new RuntimeException("FUSE starting error")
      KernelLogger.info("shell thread started")
      val (client, shutdown) = RemoteClient(remoteCommandDeps.host, port = remoteCommandDeps.port)
      for (r <- client.run(request)) {
        r.replyType match {
          case RUN_REPLY_TYPE_STDOUT => stdOut.put(r.strValue)
          case RUN_REPLY_TYPE_STDERR => stdErr.put(r.strValue)
          case RUN_REPLY_TYPE_RETURN => returnValue.success(r.intValue)
          case e                     => KernelLogger.error(s"invalid message: ${r}")
        }
      }
      shutdown()
    })
    shellThread.setName("scaleda-run-shell-thread")
    shellThread.start()
    val fsThread = new Thread(
      () => {
        var fsRunning                 = true
        var shutdown: Option[() => _] = None
        while (fsRunning) {
          try {
            val dataProvider = new FuseDataProvider(remoteCommandDeps.projectRoot)
            // val dataProvider = new FuseSimpleDataProvider(remoteCommandDeps.projectRoot)
            val (_client, stream, observer, _shutdown) =
              FuseTransferClient.asStream(dataProvider)
            shutdown = Some(_shutdown)
            try {
              stream.onNext(FuseTransferMessage.of(0, "login", ByteString.EMPTY))
              KernelLogger.info("shell thread started, wait server side fuse mount...")
              observer.initFlag.synchronized {
                observer.initFlag.wait(10000)
              }
              if (!observer.initDone) throw new RuntimeException("fuse server side mount timeout")
              KernelLogger.info("recv init signal")
              // Thread.sleep(2000)
              Thread.sleep(5000)
              // Thread.sleep(500000)
              KernelLogger.info("fs thread notifies shell thread")
              fuseStartWaits.synchronized {
                fuseStarted = true
                fuseStartWaits.notify()
              }
              while (true) {
                Thread.sleep(100)
              }
            } catch {
              case e: InterruptedException =>
                stream.onCompleted()
                throw e
            }
          } catch {
            case _e: InterruptedException =>
              KernelLogger.info("fs data provider exits")
              fsRunning = false
            case e: Throwable =>
              KernelLogger.warn("fs data provider restart", e)
              Thread.sleep(100)
          } finally {
            shutdown.foreach(f => f())
            shutdown = None
          }
          fsRunning = false
        }
      },
      "scaleda-run-fuse-thread"
    )
    fsThread.setDaemon(false)
    fsThread.start()
    shellThread.join()
    fsThread.interrupt()
    // fsThread.join()
  })

  override def run: CommandOutputStream = {
    thread.setDaemon(true)
    thread.start()
    CommandOutputStream(returnValue.future, stdOut, stdErr)
  }
}

object RemoteCommandRunner {
  def execute(
      remoteCommandDeps: RemoteCommandDeps,
      commands: Seq[CommandDeps],
      handler: ScaledaRunHandler,
      ignoreErrors: Boolean = false
  ): Unit =
    CommandRunner.executeLocalOrRemote(
      Some(remoteCommandDeps),
      commands,
      handler,
      ignoreErrors
    )
}

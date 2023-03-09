package top.criwits.scaleda
package kernel.shell.command

import kernel.net.remote.RunReplyType._
import kernel.net.remote.{RunRequest, StringTriple}
import kernel.net.{RemoteClient, RemoteServer}
import kernel.shell.ScaledaRunHandler
import kernel.utils.KernelLogger

import scala.language.existentials

case class RemoteCommandDeps(
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
    envs = deps.envs.map(t => new StringTriple(t._1, t._2))
  )
  override val thread = new Thread(() => {
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

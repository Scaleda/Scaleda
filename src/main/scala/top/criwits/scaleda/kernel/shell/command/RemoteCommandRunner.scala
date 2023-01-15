package top.criwits.scaleda
package kernel.shell.command

import kernel.net.RemoteServer
import kernel.net.remote.RunReplyType._
import kernel.net.remote.{RemoteGrpc, RunRequest, StringTriple}
import kernel.shell.ScaledaRunHandler
import kernel.utils.KernelLogger

import io.grpc.ManagedChannelBuilder

import scala.language.existentials

case class RemoteCommandDeps(
    host: String = "localhost",
    port: Int = RemoteServer.port
)

class RemoteCommandRunner(
    deps: CommandDeps,
    remoteCommandDeps: RemoteCommandDeps
) extends CommandRunner(deps)
    with AbstractCommandRunner {
  val builder = ManagedChannelBuilder.forAddress(
    remoteCommandDeps.host,
    remoteCommandDeps.port
  )
  builder.usePlaintext()
  val request = new RunRequest(
    deps.command,
    deps.path,
    deps.envs.map(t => new StringTriple(t._1, t._2))
  )
  override val thread = new Thread(() => {
    val channel = builder.build()
    val stub = RemoteGrpc.blockingStub(channel)
    for (r <- stub.run(request)) {
      r.replyType match {
        case RUN_REPLY_TYPE_STDOUT => stdOut.put(r.strValue)
        case RUN_REPLY_TYPE_STDERR => stdErr.put(r.strValue)
        case RUN_REPLY_TYPE_RETURN => returnValue.success(r.intValue)
        case e                     => KernelLogger.error(s"invalid message: ${r}")
      }
    }
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

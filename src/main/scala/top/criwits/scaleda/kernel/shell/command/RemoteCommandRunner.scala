package top.criwits.scaleda
package kernel.shell.command

import kernel.net.RemoteServer
import kernel.net.remote.{RemoteGrpc, RunReplyType, RunRequest, StringTriple}
import kernel.utils.KernelLogger

import io.grpc.ManagedChannelBuilder

import scala.language.existentials

case class RemoteCommandDeps
(host: String = "localhost", port: Int = RemoteServer.port)

class RemoteCommandRunner(deps: CommandDeps, remoteCommandDeps: RemoteCommandDeps)
  extends CommandRunner(deps) with AbstractCommandRunner {
  val builder = ManagedChannelBuilder.forAddress(remoteCommandDeps.host, remoteCommandDeps.port)
  builder.usePlaintext()
  val request = new RunRequest(deps.command, deps.path, deps.envs.map(t => new StringTriple(t._1, t._2)))
  val thread = new Thread(() => {
    val channel = builder.build()
    val stub = RemoteGrpc.blockingStub(channel)
    for (r <- stub.run(request)) {
      r.replyType match {
        case RunReplyType.STDOUT => stdOut.put(r.strValue)
        case RunReplyType.STDERR => stdErr.put(r.strValue)
        case RunReplyType.RETURN => returnValue.success(r.intValue)
        case e => KernelLogger.error(s"invalid message: ${r}")
      }
    }
  })

  override def run: CommandOutputStream = {
    thread.setDaemon(true)
    thread.start()
    CommandOutputStream(returnValue.future, stdOut, stdErr)
  }
}

package top.criwits.scaleda
package kernel.net

import kernel.net.remote.RunReplyType.{RUN_REPLY_TYPE_RETURN, RUN_REPLY_TYPE_STDERR, RUN_REPLY_TYPE_STDOUT}
import kernel.net.remote._
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.toolchain.Toolchain
import kernel.utils.{KernelLogger, OS}

import io.grpc.stub.StreamObserver
import io.grpc.{Server, ServerBuilder}
import top.criwits.scaleda.kernel.shell.ScaledaRunHandler

import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

/** Main gRPC server, will receiver commands from client and execute them
  */
object RemoteServer {
  def start(): Unit = {
    val server = new RemoteServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

  val port = 50051
}

class RemoteServer(executionContext: ExecutionContext) {
  self =>
  private[this] var server: Option[Server] = None

  private def start(): Unit = {
    val builder = ServerBuilder.forPort(RemoteServer.port)
    builder.addService(RemoteGrpc.bindService(new RemoteImpl, executionContext))
    server = Some(builder.build().start())
    KernelLogger.logger.info(
      "Server started, listening on " + RemoteServer.port
    )
    sys.addShutdownHook {
      System.err.println(
        "*** shutting down gRPC server since JVM is shutting down"
      )
      self.stop()
      System.err.println("*** server shut down")
    }
  }

  private def stop(): Unit =
    server.foreach(_.shutdown())

  private def blockUntilShutdown(): Unit =
    server.foreach(_.awaitTermination())

  private class RemoteImpl extends RemoteGrpc.Remote {
    override def run(
        request: RunRequest,
        responseObserver: StreamObserver[RunReply]
    ): Unit = {
      CommandRunner.execute(Seq(CommandDeps(
        request.command,
        request.path,
        request.envs.map(t => (t.a, t.b))
      )), new ScaledaRunHandler {
        override def onStdout(data: String) = {
          KernelLogger.info("[remote executor stdout]", data)
          responseObserver.onNext(
            new RunReply(RUN_REPLY_TYPE_STDOUT, strValue = data)
          )
        }

        override def onStderr(data: String) = {
          KernelLogger.warn("[remote executor stderr]", data)
          responseObserver.onNext(
            new RunReply(RUN_REPLY_TYPE_STDERR, strValue = data)
          )
        }

        override def onReturn(returnValue: Int, finishedAll: Boolean) = {
          responseObserver.onNext(
            new RunReply(
              RUN_REPLY_TYPE_RETURN,
              intValue = returnValue
            )
          )
        }
      })
      responseObserver.onCompleted()
    }

    override def getProfiles(request: Empty): Future[ProfilesReply] = async {
      ProfilesReply(
        Toolchain
          .profiles()
          .toSeq
          .map(p =>
            RemoteProfile(
              name = p.profileName,
              toolchainType = p.toolchainType
            )
          )
      )
    }

    override def getRemoteInfo(request: Empty): Future[RemoteInfo] = async {
      RemoteInfo(os = OS.getOSType match {
        case OS.Windows => RemoteOsType.REMOTE_OS_TYPE_WINDOWS
        case OS.MacOS => RemoteOsType.REMOTE_OS_TYPE_MACOS
        case OS.Unix => RemoteOsType.REMOTE_OS_TYPE_LINUX
      })
    }
  }
}

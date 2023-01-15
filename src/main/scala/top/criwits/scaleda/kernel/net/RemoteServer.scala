package top.criwits.scaleda
package kernel.net

import kernel.net.remote.RunReplyType.{
  RUN_REPLY_TYPE_RETURN,
  RUN_REPLY_TYPE_STDERR,
  RUN_REPLY_TYPE_STDOUT
}
import kernel.net.remote._
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.toolchain.Toolchain
import kernel.utils.KernelLogger

import io.grpc.stub.StreamObserver
import io.grpc.{Server, ServerBuilder}

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
      val runner = new CommandRunner(
        CommandDeps(
          request.command,
          request.path,
          request.envs.map(t => (t.a, t.b))
        )
      )
      val r = runner.run
      while (!r.returnValue.isCompleted) {
        r.stdOut.forEach(s => {
          KernelLogger.info(s)
          responseObserver.onNext(
            new RunReply(RUN_REPLY_TYPE_STDOUT, strValue = s)
          )
        })
        r.stdErr.forEach(s => {
          KernelLogger.error(s)
          responseObserver.onNext(
            new RunReply(RUN_REPLY_TYPE_STDERR, strValue = s)
          )
        })
        Thread.sleep(300)
      }
      KernelLogger.info(s"return value: ${r.returnValue.value.get}")
      responseObserver.onNext(
        new RunReply(
          RUN_REPLY_TYPE_RETURN,
          intValue = r.returnValue.value.get.get
        )
      )
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
  }
}

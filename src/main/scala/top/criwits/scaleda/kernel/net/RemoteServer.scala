package top.criwits.scaleda
package kernel.net

import kernel.net.remote.{RemoteGrpc, RunReply, RunReplyType, RunRequest}
import kernel.shell.command.CommandRunner
import kernel.utils.KernelLogger

import io.grpc.stub.StreamObserver
import io.grpc.{Server, ServerBuilder}

import scala.concurrent.ExecutionContext
import scala.language.existentials

object RemoteServer {
  def start(): Unit = {
    val server = new RemoteServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

  private val port = 50051
}

class RemoteServer(executionContext: ExecutionContext) {
  self =>
  private[this] var server: Server = null

  private def start(): Unit = {
    val builder = ServerBuilder.forPort(RemoteServer.port)
    builder.addService(RemoteGrpc.bindService(new RemoteImpl, executionContext))
    server = builder.build().start()
    KernelLogger.logger.info("Server started, listening on " + RemoteServer.port)
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down")
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class RemoteImpl extends RemoteGrpc.Remote {
    override def run(request: RunRequest, responseObserver: StreamObserver[RunReply]): Unit = {
      val runner = new CommandRunner(request.command, request.path, request.envs.map(t => (t.a, t.b)))
      val r = runner.run
      while (!r.returnValue.isCompleted) {
        r.stdOut.forEach(s => {
          KernelLogger.info(s)
          responseObserver.onNext(new RunReply(RunReplyType.STDOUT, strValue = s))
        })
        r.stdErr.forEach(s => {
          KernelLogger.error(s)
          responseObserver.onNext(new RunReply(RunReplyType.STDERR, strValue = s))
        })
        Thread.sleep(300)
      }
      KernelLogger.info(s"return value: ${r.returnValue.value.get}")
      responseObserver.onNext(new RunReply(RunReplyType.RETURN, intValue = r.returnValue.value.get.get))
      responseObserver.onCompleted()
    }
  }
}


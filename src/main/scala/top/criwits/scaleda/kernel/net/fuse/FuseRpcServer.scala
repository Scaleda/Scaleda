package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.RemoteServer
import kernel.net.fuse.fs.RemoteFuseGrpc
import kernel.utils.KernelLogger

import io.grpc.{Server, ServerBuilder}

import scala.concurrent.ExecutionContext
import scala.language.existentials

@Deprecated
object FuseRpcServer {
  val port = RemoteServer.DEFAULT_PORT + 1

  def start(sourcePath: String): Unit = {
    val server = new FuseRpcServer(ExecutionContext.global)
    server.start(sourcePath)
    server.blockUntilShutdown()
  }
}

@Deprecated
class FuseRpcServer(executionContext: ExecutionContext) { self =>
  private[this] var server: Option[Server] = None

  private def start(sourcePath: String): Unit = {
    val builder = ServerBuilder.forPort(FuseRpcServer.port)
    builder.addService(
      RemoteFuseGrpc.bindService(
        new FuseDataProvider(sourcePath),
        executionContext
      )
    )
    server = Some(builder.build().start())
    KernelLogger.info(
      "Server started, listening on " + FuseRpcServer.port
    )
    sys.addShutdownHook {
      System.err.println(
        "*** shutting down gRPC server since JVM is shutting down"
      )
      self.stop()
      System.err.println("*** server shut down")
    }
  }

  private def stop(): Unit = server.foreach(_.shutdown())
  private def blockUntilShutdown(): Unit = server.foreach(_.awaitTermination())
}

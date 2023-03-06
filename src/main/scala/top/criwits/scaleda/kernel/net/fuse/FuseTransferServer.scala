package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.RpcPatch
import kernel.net.fuse.fs.RemoteFuseTransferGrpc.RemoteFuseTransfer
import kernel.net.fuse.fs.{FuseLoginMessage, RemoteFuseTransferGrpc}
import kernel.utils.KernelLogger

import io.grpc.stub.StreamObserver

import scala.concurrent.ExecutionContext

class FuseTransferServer extends RemoteFuseTransfer {
  override def login(responseObserver: StreamObserver[FuseLoginMessage]): StreamObserver[FuseLoginMessage] = {
    KernelLogger.info("server: login")
    new StreamObserver[FuseLoginMessage] {
      override def onNext(value: FuseLoginMessage) = responseObserver.onNext(value)

      override def onError(t: Throwable) = responseObserver.onError(t)

      override def onCompleted() = responseObserver.onCompleted()
    }
  }
}
object FuseTransferClient {
  private final val DEFAULT_PORT = 4555
  def apply(host: String = "127.0.0.1", port: Int = DEFAULT_PORT) =
    RpcPatch.getClient(RemoteFuseTransferGrpc.stub, host, port)
}

object FuseTransferTester extends App {
  private final val TEST_PORT = 4551
  val thread = new Thread(() => {
    val server = RpcPatch.getServer(
      Seq(
        RemoteFuseTransferGrpc.bindService(new FuseTransferServer, ExecutionContext.global)
      ),
      TEST_PORT
    )
  })
  thread.start()
  Thread.sleep(500)
  val (client, shutdown) = FuseTransferClient(port = TEST_PORT)
  client.login(new StreamObserver[FuseLoginMessage] {
    override def onNext(value: FuseLoginMessage) = {
      KernelLogger.info("client: onNext", value.toProtoString)
    }

    override def onError(t: Throwable) = {
      KernelLogger.warn("client onError: ", t)
    }

    override def onCompleted() = {
      KernelLogger.warn("client: onComplete")
    }
  })
}

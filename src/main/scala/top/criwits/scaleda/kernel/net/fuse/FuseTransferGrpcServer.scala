package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.FuseLoginMessage
import kernel.net.fuse.fs.RemoteFuseTransferGrpc.RemoteFuseTransfer

import io.grpc.stub.StreamObserver

class FuseTransferGrpcServer extends RemoteFuseTransfer {
  override def login(responseObserver: StreamObserver[FuseLoginMessage]): StreamObserver[FuseLoginMessage] = {
    new StreamObserver[FuseLoginMessage] {
      override def onNext(value: FuseLoginMessage) = responseObserver.onNext(value)

      override def onError(t: Throwable) = responseObserver.onError(t)

      override def onCompleted() = responseObserver.onCompleted()
    }
  }
}

// class FuseTransferClient extends RemoteFuseTransferGrpc

object FuseTransferTester extends App {}

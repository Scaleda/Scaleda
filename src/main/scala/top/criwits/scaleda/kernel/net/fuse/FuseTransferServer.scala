package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.RpcPatch
import kernel.net.fuse.fs.RemoteFuseTransferGrpc.RemoteFuseTransfer
import kernel.net.fuse.fs._
import kernel.utils.KernelLogger
import kernel.utils.serialise.BinarySerializeHelper

import io.grpc.stub.StreamObserver

import scala.collection.mutable
import scala.concurrent.ExecutionContext

case class FuseTransferMessage(id: Long, function: String, data: Any)

class FuseTransferServer extends RemoteFuseTransfer {
  override def login(responseObserver: StreamObserver[FuseLoginMessage]): StreamObserver[FuseLoginMessage] = {
    KernelLogger.info("server: login")
    new StreamObserver[FuseLoginMessage] {
      override def onNext(value: FuseLoginMessage) = {
        KernelLogger.info("server onNext: ", value.toProtoString)
        responseObserver.onNext(value)
      }

      override def onError(t: Throwable) = {
        KernelLogger.info("server onError: ", t)
        responseObserver.onError(t)
      }

      override def onCompleted() = {
        KernelLogger.info("server onComplete")
        responseObserver.onCompleted()
      }
    }
  }
}

object FuseTransferServer {
  val recvData = new mutable.HashMap[Long, FuseTransferMessage]()
  def parseResponseType(function: String, obj: Any) = function match {
    case "init"     => obj.asInstanceOf[EmptyReq]
    case "destory"  => obj.asInstanceOf[EmptyReq]
    case "getattr"  => obj.asInstanceOf[GetAttrReply]
    case "readlink" => obj.asInstanceOf[StringTupleReply]
    case "mkdir"    => obj.asInstanceOf[IntReply]
    case "unlink"   => obj.asInstanceOf[IntReply]
    case "symlink"  => obj.asInstanceOf[IntReply]
    case "rename"   => obj.asInstanceOf[IntReply]
    case "chmod"    => obj.asInstanceOf[IntReply]
    case "read"     => obj.asInstanceOf[ReadReply]
    case "write"    => obj.asInstanceOf[IntReply]
    case "readdir"  => obj.asInstanceOf[ReaddirReply]
    case "create"   => obj.asInstanceOf[IntReply]
    case _          => obj
  }
  def parseResponseData(msg: FuseLoginMessage): Any =
    BinarySerializeHelper.fromGrpcBytes(msg.message)
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
  val stream = client.login(new StreamObserver[FuseLoginMessage] {
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
  stream.onNext(FuseLoginMessage.defaultInstance)
  Thread.sleep(500)
  stream.onCompleted()
  Thread.sleep(500)
  shutdown()
}

package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.FuseTransferClient.requestMessageInto
import kernel.net.fuse.fs._
import kernel.utils.KernelLogger
import kernel.utils.serialise.BinarySerializeHelper

import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver

import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

class FuseTransferClientObserver(dataProvider: FuseDataProvider) extends StreamObserver[FuseTransferMessage] {
  private var tx: StreamObserver[FuseTransferMessage] = _
  def setTx(stream: StreamObserver[FuseTransferMessage]): Unit =
    tx = stream
  val initFlag = new Object

  override def onNext(msg: FuseTransferMessage) = {
    KernelLogger.debug("client: onNext", msg.toProtoString)
    val req = msg.message

    def handleFutureData[T](future: Future[T]): ByteString = {
      val result = Await.result(future, 3 seconds)
      KernelLogger.info("result:", result)
      BinarySerializeHelper.fromAny(result)
    }

    val respFuture: Future[_] = msg.function match {
      case "init" =>
        initFlag.synchronized {
          initFlag.notify()
        }
        dataProvider.init(requestMessageInto(req))
      case "destroy"  => dataProvider.destroy(requestMessageInto(req))
      case "getattr"  => dataProvider.getattr(requestMessageInto(req))
      case "readlink" => dataProvider.readlink(requestMessageInto(req))
      case "mkdir"    => dataProvider.mkdir(requestMessageInto(req))
      case "unlink"   => dataProvider.unlink(requestMessageInto(req))
      case "rmdir"    => dataProvider.rmdir(requestMessageInto(req))
      case "symlink"  => dataProvider.symlink(requestMessageInto(req))
      case "rename"   => dataProvider.rename(requestMessageInto(req))
      case "chmod"    => dataProvider.chmod(requestMessageInto(req))
      case "read"     => dataProvider.read(requestMessageInto(req))
      case "write"    => dataProvider.write(requestMessageInto(req))
      case "readdir"  => dataProvider.readdir(requestMessageInto(req))
      case "create"   => dataProvider.create(requestMessageInto(req))
      case _ =>
        KernelLogger.error("Unknown function name:", msg.function)
        async {
          throw new RuntimeException(s"Unknown function name: ${msg.function}")
        }
    }
    try {
      val resp       = handleFutureData(respFuture)
      val messageNew = FuseTransferMessage.of(msg.id, msg.function, resp)
      KernelLogger.info("client onNext done")
      tx.onNext(messageNew)
    } catch {
      case e: Throwable =>
        val messageError = FuseTransferMessage.of(msg.id, "error", BinarySerializeHelper.fromAny(e))
        tx.onNext(messageError)
    }
  }

  override def onError(t: Throwable) = {
    KernelLogger.warn("client onError:", t)
    // if (tx != null) tx.onCompleted()
  }

  override def onCompleted() = {
    KernelLogger.warn("client: onComplete")
  }
}

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

class FuseTransferClientObserver(dataProvider: RemoteFuseGrpc.RemoteFuse) extends StreamObserver[FuseTransferMessage] {
  private var tx: StreamObserver[FuseTransferMessage] = _
  def setTx(stream: StreamObserver[FuseTransferMessage]): Unit =
    tx = stream
  val initFlag = new Object
  var initDone = false

  override def onNext(msg: FuseTransferMessage) = {
    KernelLogger.debug("client: onNext", msg.toProtoString)
    val req = msg.message

    def handleFutureData[T](future: Future[T]): ByteString = {
      val result = Await.result(future, 3000000 seconds)
      KernelLogger.debug("result:", result)
      BinarySerializeHelper.fromAny(result)
    }

    val respFuture: Future[_] = msg.function match {
      case "init" =>
        initFlag.synchronized {
          initDone = true
          initFlag.notify()
        }
        dataProvider.init(requestMessageInto(req))
      case "create"   => dataProvider.create(requestMessageInto(req))
      case "getattr"  => dataProvider.getattr(requestMessageInto(req))
      case "mkdir"    => dataProvider.mkdir(requestMessageInto(req))
      case "read"     => dataProvider.read(requestMessageInto(req))
      case "readdir"  => dataProvider.readdir(requestMessageInto(req))
      case "statfs"   => dataProvider.statfs(requestMessageInto(req))
      case "rename"   => dataProvider.rename(requestMessageInto(req))
      case "rmdir"    => dataProvider.rmdir(requestMessageInto(req))
      case "truncate" => dataProvider.truncate(requestMessageInto(req))
      case "unlink"   => dataProvider.unlink(requestMessageInto(req))
      case "open"     => dataProvider.`open`(requestMessageInto(req))
      case "release"  => dataProvider.release(requestMessageInto(req))
      case "write"    => dataProvider.write(requestMessageInto(req))
      case "chmod"    => dataProvider.chmod(requestMessageInto(req))
      case "destroy"  => dataProvider.destroy(requestMessageInto(req))
      case _ =>
        KernelLogger.error("Unknown function name:", msg.function)
        async {
          throw new RuntimeException(s"Unknown function name: ${msg.function}")
        }
    }
    try {
      val resp       = handleFutureData(respFuture)
      val messageNew = FuseTransferMessage.of(msg.id, msg.function, resp)
      KernelLogger.debug("client onNext done")
      tx.onNext(messageNew)
    } catch {
      case e: Throwable =>
        KernelLogger.warn("exception during client observer", e)
        e.printStackTrace()
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

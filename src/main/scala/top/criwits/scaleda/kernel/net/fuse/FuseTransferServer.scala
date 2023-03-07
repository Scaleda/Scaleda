package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.RpcPatch
import kernel.net.fuse.FuseTransferClient.requestMessageInto
import kernel.net.fuse.FuseTransferServer._
import kernel.net.fuse.fs.RemoteFuseTransferGrpc.RemoteFuseTransfer
import kernel.net.fuse.fs._
import kernel.utils.KernelLogger
import kernel.utils.serialise.BinarySerializeHelper

import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver

import java.util.concurrent.LinkedBlockingQueue
import scala.async.Async.async
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

case class FuseTransferMessage(id: Long, identifier: String, function: String, data: Any) {
  def toLoginMessage: FuseLoginMessage =
    FuseLoginMessage.of(id, identifier, function, BinarySerializeHelper.fromAny(data))
}

class FuseTransferObserver extends StreamObserver[FuseLoginMessage] {
  override def onNext(msg: FuseLoginMessage) = {
    KernelLogger.info("server onNext: ", msg.toProtoString)
    msg.function match {
      case "login" =>
        KernelLogger.info("server: login info with token ", msg.token)
        observers.put(msg.token, this)
      case _ =>
        val converted =
          FuseTransferMessage(msg.id, msg.token, msg.function, BinarySerializeHelper.fromGrpcBytes(msg.message))
        recvData.synchronized {
          recvData.put(
            msg.id,
            converted
          )
        }
        recvWait
          .get(msg.id)
          .foreach(obj => {
            obj.synchronized {
              obj.notify()
            }
          })
    }
  }

  override def onError(t: Throwable) = {
    KernelLogger.info("server onError: ", t)
    // responseObserver.onError(t)
  }

  override def onCompleted() = {
    KernelLogger.info("server onComplete")
    // responseObserver.onCompleted()
  }
}

class FuseTransferServer extends RemoteFuseTransfer {
  override def login(responseObserver: StreamObserver[FuseLoginMessage]): StreamObserver[FuseLoginMessage] = {
    KernelLogger.info("server: login established")
    new FuseTransferObserver
  }
}

object FuseTransferServer {
  val recvData          = new mutable.HashMap[Long, FuseTransferMessage]()
  val recvWait          = new mutable.HashMap[Long, Object]()
  val observers         = new mutable.HashMap[String, StreamObserver[FuseLoginMessage]]()
  private val sendQueue = new LinkedBlockingQueue[FuseTransferMessage]
  def parseResponse(function: String, obj: Any) = {
    function match {
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
  }
  def parseResponseData(msg: FuseLoginMessage): Any =
    BinarySerializeHelper.fromGrpcBytes(msg.message)

  def request(msg: FuseTransferMessage): FuseTransferMessage = {
    sendQueue.put(msg)
    val awaitable = new Object
    recvWait.synchronized {
      recvWait.put(msg.id, awaitable).foreach(o => KernelLogger.error("Same id is waiting! ", msg.id))
    }
    awaitable.synchronized {
      awaitable.wait()
    }
    val resp =
      recvData.synchronized { recvData.get(msg.id) }
    if (resp.isEmpty) KernelLogger.error("Did not recv data!")
    resp.get
  }

  val requestThread = new Thread(() => doRequestThread())

  private def doRequestThread(): Unit = {
    var done = false
    while (!done) {
      try {
        val msg = sendQueue.take()
        observers.get(msg.identifier) match {
          case Some(observer) => observer.onNext(msg.toLoginMessage)
          case None           => KernelLogger.error("Cannot send message ", msg, ", no observer!")
        }
      } catch {
        case _: InterruptedException =>
          KernelLogger.info("request thread exits.")
          done = true
      }
    }
  }
}

object FuseTransferClient {
  private final val DEFAULT_PORT = 4555
  def apply(host: String = "127.0.0.1", port: Int = DEFAULT_PORT) =
    RpcPatch.getClient(RemoteFuseTransferGrpc.stub, host, port)
  def requestMessageInto[T](msg: Any): T =
    msg.asInstanceOf[T]
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
  val (client, shutdown)                       = FuseTransferClient(port = TEST_PORT)
  val dataProvider                             = new FuseDataProvider("/tmp")
  var stream: StreamObserver[FuseLoginMessage] = _
  stream = client.login(new StreamObserver[FuseLoginMessage] {
    override def onNext(msg: FuseLoginMessage) = {
      KernelLogger.info("client: onNext", msg.toProtoString)
      val req = msg.message
      def handleFutureData[T](future: Future[T]): ByteString = {
        val result = Await.result(future, 30 seconds)
        BinarySerializeHelper.fromAny(result)
      }
      val respFuture: Future[_] = msg.function match {
        case "init"     => dataProvider.init(requestMessageInto(req))
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
          KernelLogger.error("Unknown function name: ", msg.function)
          async {}
      }
      val resp       = handleFutureData(respFuture)
      val messageNew = FuseLoginMessage.of(msg.id, msg.token, msg.function, resp)
      stream.onNext(messageNew)
    }

    override def onError(t: Throwable) = {
      KernelLogger.warn("client onError: ", t)
    }

    override def onCompleted() = {
      KernelLogger.warn("client: onComplete")
    }
  })
  stream.onNext(FuseLoginMessage.of(0, "token", "login", ByteString.EMPTY))
  Thread.sleep(500)
  FuseTransferServer.requestThread.start()
  val resp = FuseTransferServer.request(FuseTransferMessage(1, "token", "init", EmptyReq()))
  KernelLogger.info("resp: ", resp)
  Thread.sleep(500)
  stream.onCompleted()
  Thread.sleep(500)
  FuseTransferServer.requestThread.interrupt()
  shutdown()
}

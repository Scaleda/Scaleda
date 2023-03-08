package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.RpcPatch
import kernel.net.fuse.FuseTransferClient.requestMessageInto
import kernel.net.fuse.FuseTransferServer._
import kernel.net.fuse.fs.RemoteFuseTransferGrpc.RemoteFuseTransfer
import kernel.net.fuse.fs._
import kernel.net.user.JwtAuthorizationInterceptor
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

case class FuseTransferMessageCase(
    id: Long,
    identifier: String,
    function: String,
    data: Any,
    error: Option[Throwable] = None
) {
  def toMessage: FuseTransferMessage =
    FuseTransferMessage.of(id, function, BinarySerializeHelper.fromAny(data))
}

class FuseTransferServerObserver(val tx: StreamObserver[FuseTransferMessage])
    extends StreamObserver[FuseTransferMessage] {
  private var identifier: Option[String] = None
  override def onNext(msg: FuseTransferMessage) = {
    KernelLogger.info("server onNext: ", msg.toProtoString)
    msg.function match {
      case "login" =>
        val user = JwtAuthorizationInterceptor.USERNAME_CONTEXT_KEY.get()
        KernelLogger.info("visit from user: ", user)
        identifier = Some(user.getUsername)
        observers.put(user.getUsername, this)
      case "error" =>
        val e: Throwable = BinarySerializeHelper.fromGrpcBytes(msg.message)
        KernelLogger.warn("server recv error from client:", e)
        val converted =
          FuseTransferMessageCase(
            msg.id,
            // TODO
            "username",
            msg.function,
            (),
            error = Some(BinarySerializeHelper.fromGrpcBytes(msg.message))
          )
        // notify errors
        recvData.synchronized {
          recvData.put(msg.id, converted)
        }
        recvWait
          .get(msg.id)
          .foreach(obj => {
            KernelLogger.info("notifying error", msg.id)
            obj.synchronized {
              obj.notify()
            }
          })
      case _ =>
        val converted =
          FuseTransferMessageCase(msg.id, "username", msg.function, BinarySerializeHelper.fromGrpcBytes(msg.message))

        KernelLogger.info("converted:", converted)
        recvData.synchronized {
          recvData.put(
            msg.id,
            converted
          )
        }
        recvWait
          .get(msg.id)
          .foreach(obj => {
            KernelLogger.info("notifying", msg.id)
            obj.synchronized {
              obj.notify()
            }
          })
    }
  }

  override def onError(t: Throwable): Unit = {
    KernelLogger.info("server onError: ", t)
  }

  override def onCompleted() = {
    KernelLogger.info("server onComplete")
    identifier.foreach(observers.remove)
  }
}

class FuseTransferClientObserver(dataProvider: FuseDataProvider) extends StreamObserver[FuseTransferMessage] {
  private var tx: StreamObserver[FuseTransferMessage]          = _
  def setTx(stream: StreamObserver[FuseTransferMessage]): Unit = tx = stream

  override def onNext(msg: FuseTransferMessage) = {
    KernelLogger.info("client: onNext", msg.toProtoString)
    val req = msg.message

    def handleFutureData[T](future: Future[T]): ByteString = {
      KernelLogger.info("future:", future)
      val result = Await.result(future, 3 seconds)
      KernelLogger.info("result:", result)
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
        KernelLogger.error("Unknown function name:", msg.function)
        // throw new RuntimeException("Unknown function name")
        async {
          throw new RuntimeException("Unknown function name")
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
  }

  override def onCompleted() = {
    KernelLogger.warn("client: onComplete")
  }
}

class FuseTransferServer extends RemoteFuseTransfer {
  override def visit(responseObserver: StreamObserver[FuseTransferMessage]): StreamObserver[FuseTransferMessage] = {
    KernelLogger.info("server: visit established")
    new FuseTransferServerObserver(responseObserver)
  }
}

object FuseTransferServer {
  val recvData          = new mutable.HashMap[Long, FuseTransferMessageCase]()
  val recvWait          = new mutable.HashMap[Long, Object]()
  val observers         = new mutable.HashMap[String, FuseTransferServerObserver]()
  private val sendQueue = new LinkedBlockingQueue[FuseTransferMessageCase]
  def request(msg: FuseTransferMessageCase): FuseTransferMessageCase = {
    sendQueue.put(msg)
    val awaitable = new Object
    recvWait.synchronized {
      recvWait.put(msg.id, awaitable).foreach(o => KernelLogger.error("Same id is waiting! ", msg.id))
    }
    awaitable.synchronized {
      awaitable.wait()
    }
    recvWait.synchronized {
      recvWait.remove(msg.id)
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
          case Some(observer) => observer.tx.onNext(msg.toMessage)
          case None           => KernelLogger.error("Cannot send message ", msg, ", no observer!")
        }
      } catch {
        case _: InterruptedException =>
          KernelLogger.info("request thread exits.")
          done = true
        case e: Throwable =>
          KernelLogger.warn("request thread error, retry.", e)
          e.printStackTrace()
      }
    }
  }
}

object FuseTransferClient {
  private final val DEFAULT_PORT = 4555
  def apply(host: String = "127.0.0.1", port: Int = DEFAULT_PORT) =
    RpcPatch.getClient(RemoteFuseTransferGrpc.stub, host, port, enableAuthProvide = true)
  def asStream(dataProvider: FuseDataProvider, host: String = "127.0.0.1", port: Int = DEFAULT_PORT) = {
    val (client, shutdown) = FuseTransferClient(host, port)
    val clientStream       = new FuseTransferClientObserver(dataProvider)
    val stream             = client.visit(clientStream)
    clientStream.setTx(stream)
    (stream, shutdown)
  }
  def requestMessageInto[T](msg: ByteString): T =
    BinarySerializeHelper.fromGrpcBytes(msg).asInstanceOf[T]
}

object FuseTransferTester extends App {
  private final val TEST_PORT = 4551
  val thread = new Thread(() => {
    val server = RpcPatch.getStartServer(
      Seq(
        RemoteFuseTransferGrpc.bindService(new FuseTransferServer, ExecutionContext.global)
      ),
      TEST_PORT,
      enableAuthCheck = true
    )
  })
  thread.start()
  Thread.sleep(500)
  val (stream, shutdown) = FuseTransferClient.asStream(new FuseDataProvider("/tmp"), port = TEST_PORT)
  stream.onNext(FuseTransferMessage.of(0, "login", ByteString.EMPTY))
  Thread.sleep(100)
  FuseTransferServer.requestThread.start()
  // val resp = FuseTransferServer.request(FuseTransferMessage(1, "token", "init", EmptyReq()))
  val resp =
    FuseTransferServer.request(FuseTransferMessageCase(1, "token", "create", PathModeRequest.of("test", 0x1ed)))
  KernelLogger.info("resp: ", resp)
  // Thread.sleep(500)
  stream.onCompleted()
  // Thread.sleep(500)
  FuseTransferServer.requestThread.interrupt()
  shutdown()
}

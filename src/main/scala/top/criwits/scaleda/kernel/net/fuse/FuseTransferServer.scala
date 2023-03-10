package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.RpcPatch
import kernel.net.fuse.fs.RemoteFuseTransferGrpc.RemoteFuseTransfer
import kernel.net.fuse.fs._
import kernel.server.ScaledaServerMainRunTest
import kernel.shell.ScaledaShellMain
import kernel.utils.{KernelLogger, Paths}

import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver

import java.util.concurrent.LinkedBlockingQueue
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, TimeoutException}
import scala.language.postfixOps

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
  val fsProxies         = new mutable.HashMap[String, ServerSideFuse]()
  private val sendQueue = new LinkedBlockingQueue[FuseTransferMessageCase]
  def request(msg: FuseTransferMessageCase): FuseTransferMessageCase = {
    sendQueue.put(msg)
    val awaitable = new Object
    recvWait.synchronized {
      recvWait.put(msg.id, awaitable).foreach(o => KernelLogger.error("Same id is waiting! ", msg.id, o))
    }
    awaitable.synchronized {
      // shorter wait time, for destroy message usually not receivable
      if (msg.function == "destroy") awaitable.wait(300)
      else awaitable.wait(5000)
    }
    recvWait.synchronized {
      recvWait.remove(msg.id)
    }
    val resp =
      recvData.synchronized { recvData.get(msg.id) }
    if (resp.isEmpty) {
      KernelLogger.error(s"Did not recv data! Timeout for message req $msg")
      msg.copy(error = Some(new TimeoutException()))
    } else {
      resp.get
    }
  }

  val requestThread = new Thread(() => doRequestThread())

  private def doRequestThread(): Unit = {
    var done = false
    while (!done) {
      try {
        val msg = sendQueue.take()
        observers.synchronized {
          observers.get(msg.identifier) match {
            case Some(observer) => observer.tx.onNext(msg.toMessage)
            case None           => KernelLogger.error("Cannot send message ", msg, ", no observer!")
          }
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

  // def localRequest(
  //     localObserver: StreamObserver[FuseTransferMessage],
  //     msg: FuseTransferMessageCase
  // ): FuseTransferMessageCase = {
  //   localObserver.onNext(msg.toMessage)
  // }
}

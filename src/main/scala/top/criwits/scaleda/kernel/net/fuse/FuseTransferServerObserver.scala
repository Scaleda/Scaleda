package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.FuseTransferServer.{fsProxies, observers, recvData, recvWait}
import kernel.net.fuse.fs.FuseTransferMessage
import kernel.net.user.JwtAuthorizationInterceptor
import kernel.utils.serialise.BinarySerializeHelper
import kernel.utils.{KernelLogger, Paths}

import io.grpc.stub.StreamObserver

import java.io.File

class FuseTransferServerObserver(val tx: StreamObserver[FuseTransferMessage])
    extends StreamObserver[FuseTransferMessage] {
  private var identifier: Option[String] = None
  override def onNext(msg: FuseTransferMessage) = {
    KernelLogger.debug("server onNext: ", msg.toProtoString)
    val user = JwtAuthorizationInterceptor.USERNAME_CONTEXT_KEY.get()
    if (user == null) KernelLogger.warn("Fuse Transfer Server recv null user")
    val key = if (user == null || (user != null && user.getUsername == null)) "test" else user.getUsername
    msg.function match {
      case "login" =>
        KernelLogger.info("visit from user:", user, "key", key)
        identifier = Some(key)
        observers.synchronized {
          observers.put(key, this)
        }
        fsProxies.synchronized {
          FuseUtils.loadLibraries()
          val fs   = new ServerSideFuse(new FuseDataProxy(key))
          val dest = new File(Paths.getServerTemporalDir(), key)
          FuseUtils.mountFs(fs, dest.getAbsolutePath, blocking = false)
          fsProxies.put(key, fs)
        }
      case "error" =>
        val e: Throwable = BinarySerializeHelper.fromGrpcBytes(msg.message)
        KernelLogger.warn("server recv error from client:", e)
        val converted =
          FuseTransferMessageCase(
            msg.id,
            key,
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
          FuseTransferMessageCase(msg.id, key, msg.function, BinarySerializeHelper.fromGrpcBytes(msg.message))

        KernelLogger.debug("converted:", converted)
        recvData.synchronized {
          recvData.put(
            msg.id,
            converted
          )
        }
        recvWait
          .get(msg.id)
          .foreach(obj => {
            KernelLogger.debug("notifying message id", msg.id)
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
    observers.synchronized {
      identifier.foreach(observers.remove)
    }
    fsProxies.synchronized {
      identifier.foreach(i => {
        fsProxies.get(i).foreach(p => p.umount())
        fsProxies.remove(i)
      })
    }
  }
}

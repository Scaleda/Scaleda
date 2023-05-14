package top.scaleda
package kernel.net.fuse

import kernel.auth.AuthorizationHasher
import kernel.net.fuse.FuseTransferServer.{fsProxies, observers, recvData, recvWait}
import kernel.net.fuse.fs.FuseTransferMessage
import kernel.net.user.JwtAuthorizationInterceptor
import kernel.utils.serialise.BinarySerializeHelper
import kernel.utils.{KernelLogger, Paths}

import io.grpc.stub.StreamObserver
import org.apache.commons.codec.digest.DigestUtils

import java.io.File

class FuseTransferServerObserver(val tx: StreamObserver[FuseTransferMessage])
    extends StreamObserver[FuseTransferMessage] {
  private var identifier: Option[String] = None
  private var runId: Option[String]      = None
  override def onNext(msg: FuseTransferMessage) = {
    KernelLogger.debug("server onNext: ", msg.toProtoString)
    val user = JwtAuthorizationInterceptor.USERNAME_CONTEXT_KEY.get()
    if (user == null) KernelLogger.warn("Fuse Transfer Server recv null user")
    msg.function match {
      case "login" =>
        val runIdHashed: String =
          try {
            AuthorizationHasher.encodeString(BinarySerializeHelper.fromGrpcBytes(msg.message))
          } catch {
            case e: Throwable =>
              KernelLogger.warn("failed to hash runId, fallback:", e)
              // directly hash bytes... assert string binary is same
              DigestUtils.sha256Hex(msg.message.toByteArray)
          }
        val key = (if (user == null || (user != null && user.getUsername == null)) "test"
                   else user.getUsername) + "-" + runIdHashed
        KernelLogger.info("visit from user:", user, "runIdHashed", runIdHashed, "key", key)
        identifier = Some(key)
        runId = Some(runIdHashed)
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
            identifier.get,
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
          FuseTransferMessageCase(
            msg.id,
            identifier.get,
            msg.function,
            BinarySerializeHelper.fromGrpcBytes(msg.message)
          )

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

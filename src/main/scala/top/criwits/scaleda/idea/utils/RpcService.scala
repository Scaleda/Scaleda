package top.criwits.scaleda
package idea.utils

import kernel.utils.KernelLogger

import com.intellij.openapi.Disposable
import io.grpc.{ManagedChannelBuilder, Server, ServerBuilder}
import scaleda.scaleda.{ScaledaEmpty, ScaledaGotoSource, ScaledaRpcGrpc}

import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

class ScaledaRpcServerImpl extends ScaledaRpcGrpc.ScaledaRpc {
  override def ping(request: ScaledaEmpty): Future[ScaledaEmpty] = async {
    KernelLogger.info("grpc ping")
    ScaledaEmpty.of()
  }

  override def gotoSource(request: ScaledaGotoSource): Future[ScaledaEmpty] = async {
    KernelLogger.info(s"grpc gotoSource($request)")
    ScaledaEmpty.of()
  }
}

class RpcService extends Disposable {
  private final val DEFAULT_PORT = 4151

  var stop                   = false
  var server: Option[Server] = None
  new Thread(() => {
    while (!stop) {
      try {
        val provider = RpcPatch.getDefaultServerProvider
        val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
        method.setAccessible(true)
        val builder = method.invoke(provider, DEFAULT_PORT).asInstanceOf[ServerBuilder[_]]
        builder.addService(ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl, ExecutionContext.global))
        MainLogger.info("scaleda grpc server serve at port", DEFAULT_PORT)
        server = Some(builder.build().start())
        server.get.awaitTermination()
      } catch {
        case _e: Throwable => {
          MainLogger.warn("trying", _e)
          _e.printStackTrace()
          Thread.sleep(3000)
        }
      }
    }
  }).start()

  override def dispose() = {
    stop = true;
    server.foreach(s => s.shutdown())
    server = None
  }
}

object RpcServiceTest extends App {
  private final val DEFAULT_PORT = 4151
  val loader                     = getClass.getClassLoader
  KernelLogger.info(s"loader: $loader")
  private val usePatch = false
  val builder = if (usePatch) {
    val provider = RpcPatch.getDefaultServerProvider
    val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
    method.setAccessible(true)
    method.invoke(provider, DEFAULT_PORT).asInstanceOf[ServerBuilder[_]]
  } else {
    ServerBuilder.forPort(DEFAULT_PORT)
  }
  builder.addService(ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl, ExecutionContext.global))
  KernelLogger.info("before start()")
  val server = builder.build().start()
  val serverThread = new Thread(() => {
    server.awaitTermination()
  })
  serverThread.start()

  private val clientBuilder = ManagedChannelBuilder.forAddress("127.0.0.1", DEFAULT_PORT)
  clientBuilder.usePlaintext()
  val channel = clientBuilder.build()
  val stub = ScaledaRpcGrpc.blockingStub(channel)
  stub.ping(ScaledaEmpty.of())
  channel.shutdownNow()

  server.shutdownNow()
  KernelLogger.info("done")
}


object RpcServiceClientTest extends App {
  private final val DEFAULT_PORT = 4151
    private val clientBuilder = ManagedChannelBuilder.forAddress("127.0.0.1", DEFAULT_PORT)
  clientBuilder.usePlaintext()
  val channel = clientBuilder.build()
  val stub = ScaledaRpcGrpc.blockingStub(channel)
  stub.ping(ScaledaEmpty.of())
  channel.shutdownNow()
  KernelLogger.info("done")
}

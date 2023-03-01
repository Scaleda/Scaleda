package top.criwits.scaleda
package idea.utils

import kernel.utils.KernelLogger

import com.intellij.openapi.Disposable
import io.grpc.{Server, ServerBuilder}
import scaleda.scaleda.{ScaledaEmpty, ScaledaGotoSource, ScaledaRpcGrpc}

import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

class ScaledaRpcServerImpl extends ScaledaRpcGrpc.ScaledaRpc {
  override def ping(request: ScaledaEmpty): Future[ScaledaEmpty] = async {
    ScaledaEmpty.of()
  }

  override def gotoSource(request: ScaledaGotoSource): Future[ScaledaEmpty] = async {
    ScaledaEmpty.of()
  }
}

class RpcService extends Disposable {
  private final val DEFAULT_PORT = 4151

  var stop                   = false
  var server: Option[Server] = None
  // new Thread(() => {
  //   while (!stop) {
  //     try {
  // val cl: ClassLoader = Thread.currentThread.getContextClassLoader
  // val clazz = cl.loadClass("io.grpc.ServerProvider")
  // val clazz = cl.loadClass("io.grpc.ServerBuilder")
  // val builder = clazz.getMethod("forPort").invoke(DEFAULT_PORT).asInstanceOf[ServerBuilder]
  val loader = getClass.getClassLoader
  KernelLogger.info(s"loader: $loader")
  // val builder = ServerBuilder.forPort(DEFAULT_PORT)
  private val provider = RpcServicePatch.getDefaultServerProvider
  private val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
  method.setAccessible(true)
  val builder = method.invoke(provider, DEFAULT_PORT).asInstanceOf[ServerBuilder[_]]
  builder.addService(ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl, ExecutionContext.global))
  server = Some(builder.build().start())
  MainLogger.info("scaleda grpc server serve at port", DEFAULT_PORT)
  server.get.awaitTermination()
  //     } catch {
  //       case _e: Throwable => {
  //         MainLogger.debug("trying", _e)
  //         _e.printStackTrace()
  //         Thread.sleep(3000)
  //       }
  //     }
  //   }
  // }).start()

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
  // val _builder = ServerBuilder.forPort(4151)
  private val provider = RpcServicePatch.getDefaultServerProvider
  private val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
  method.setAccessible(true)
  val builder = method.invoke(provider, DEFAULT_PORT).asInstanceOf[ServerBuilder[_]]
  builder.addService(ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl, ExecutionContext.global))
  val server = builder.build().start()
  server.awaitTermination()
  KernelLogger.info("done")
}

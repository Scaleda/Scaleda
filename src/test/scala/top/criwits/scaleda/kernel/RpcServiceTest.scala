package top.criwits.scaleda
package kernel

import idea.utils.ScaledaRpcServerImpl
import kernel.net.RpcPatch
import kernel.utils.KernelLogger

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scaleda.scaleda.{ScaledaEmpty, ScaledaRpcGrpc}

import scala.concurrent.ExecutionContext

class RpcServiceTest extends AnyFlatSpec with should.Matchers {
  it should "test run" in {
    val DEFAULT_PORT = 4151
    val loader       = getClass.getClassLoader
    KernelLogger.info(s"loader: $loader")
    KernelLogger.info("before start()")
    val server = RpcPatch.getStartServer(
      Seq(ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl(() => null), ExecutionContext.global)),
      DEFAULT_PORT,
      enableAuthCheck = false,
      useReflection = true
    )
    val serverThread = new Thread(() => {
      server.awaitTermination()
    })
    serverThread.start()

    val (client, shutdown) =
      RpcPatch.getClient(ScaledaRpcGrpc.blockingStub, "localhost", DEFAULT_PORT, enableAuthProvide = false)
    client.ping(ScaledaEmpty())
    shutdown()

    server.shutdownNow()
    KernelLogger.info("done")
  }
}

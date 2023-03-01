package top.criwits.scaleda
package kernel.rvcd

import idea.utils.RpcPatch
import kernel.utils.KernelLogger

import io.grpc.ManagedChannelBuilder
import rvcd.rvcd.{RvcdOpenFile, RvcdRpcGrpc}

import scala.language.existentials

object RvcdClient {
  private final val DEFAULT_PORT = 5411
  def apply(host: String = "127.0.0.1", port: Int = DEFAULT_PORT) = {
    val provider = RpcPatch.getDefaultClientProvider
    val method   = provider.getClass.getDeclaredMethod("builderForAddress", classOf[String], Integer.TYPE)
    method.setAccessible(true)
    val builder = method.invoke(provider, host, port).asInstanceOf[ManagedChannelBuilder[_]]
    // val builder = ManagedChannelBuilder.forAddress(host, port)
    builder.usePlaintext()
    val channel = builder.build()
    (RvcdRpcGrpc.blockingStub(channel), () => channel.shutdownNow())
  }
}

object RvcdTest extends App {
  val (client, shutdown) = RvcdClient()
  client.openFile(RvcdOpenFile.of("/home/chiro/programs/rvcd/data/testbench0.vcd"))
  KernelLogger.info("done")
  shutdown()
}

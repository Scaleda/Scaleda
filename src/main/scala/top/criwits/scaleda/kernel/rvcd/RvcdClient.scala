package top.criwits.scaleda
package kernel.rvcd

import io.grpc.{ManagedChannelBuilder, StatusRuntimeException}
import rvcd.rvcd.{RvcdOpenFile, RvcdRpcGrpc}
import top.criwits.scaleda.kernel.utils.KernelLogger

import scala.language.existentials

object RvcdClient {
  private final val DEFAULT_PORT = 5411
  def apply(host: String, port: Int = DEFAULT_PORT) = {
    val builder = ManagedChannelBuilder.forAddress(host, port)
    builder.usePlaintext()
    val channel = builder.build()
    RvcdRpcGrpc.blockingStub(channel)
  }
}

object RvcdTest extends App {
  private val stub = RvcdClient("127.0.0.1")
  stub.openFile(RvcdOpenFile.of("/home/chiro/programs/rvcd/data/testbench0.vcd"))
  KernelLogger.info("done")
}

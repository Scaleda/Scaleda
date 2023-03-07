package top.criwits.scaleda
package kernel.rvcd

import kernel.net.RpcPatch

import rvcd.rvcd.RvcdRpcGrpc

import scala.language.existentials

object Rvcd {
  private final val DEFAULT_PORT = 5411
  def apply(host: String = "127.0.0.1", port: Int = DEFAULT_PORT) =
    RpcPatch.getClient(RvcdRpcGrpc.blockingStub, host, port)
}

// object RvcdTest extends App {
//   import kernel.utils.KernelLogger
//
//   import rvcd.rvcd.RvcdOpenFile
//   val (client, shutdown) = Rvcd()
//   client.openFile(RvcdOpenFile.of("/home/chiro/programs/rvcd/data/testbench0.vcd"))
//   KernelLogger.info("done")
//   shutdown()
// }

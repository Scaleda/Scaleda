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

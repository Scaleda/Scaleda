package top.criwits.scaleda
package kernel.net

import kernel.net.remote.RemoteGrpc

import scala.language.existentials

object RemoteClient {
  def apply(host: String, port: Int = RemoteServer.DEFAULT_PORT) =
    RpcPatch.getClient(RemoteGrpc.blockingStub, host, port, enableAuthProvide = true)
}

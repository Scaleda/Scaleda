package top.criwits.scaleda
package kernel.net

import io.grpc.ManagedChannelBuilder
import top.criwits.scaleda.kernel.net.remote.RemoteGrpc
import scala.language.existentials

object RemoteClient {
  def apply(host: String, port: Int) = {
    val builder = ManagedChannelBuilder.forAddress(host, port)
    builder.usePlaintext()
    val channel = builder.build()
    RemoteGrpc.blockingStub(channel)
  }
}

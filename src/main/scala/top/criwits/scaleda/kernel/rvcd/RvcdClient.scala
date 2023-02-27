package top.criwits.scaleda
package kernel.rvcd

import io.grpc.ManagedChannelBuilder

import scala.language.existentials

object RvcdClient {
  def apply(host: String, port: Int) = {
    val builder = ManagedChannelBuilder.forAddress(host, port)
    builder.usePlaintext()
    val channel = builder.build()
    RvcdClientGrpc.blockingStub(channel)
  }
}

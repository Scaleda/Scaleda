package top.criwits.scaleda
package kernel.net

import io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader
import io.grpc.{Channel, ManagedChannelBuilder, ManagedChannelProvider, ServerProvider}

import scala.language.existentials

object RpcPatch {
  def getDefaultServerProvider =
    getCandidatesViaServiceLoader(classOf[ServerProvider], classOf[ServerProvider].getClassLoader).iterator.next
  def getDefaultClientProvider = getCandidatesViaServiceLoader(
    classOf[ManagedChannelProvider],
    classOf[ManagedChannelProvider].getClassLoader
  ).iterator.next
  def getClient[T](channelType: Channel => T, host: String, port: Int) = {
    val provider = RpcPatch.getDefaultClientProvider
    val method   = provider.getClass.getDeclaredMethod("builderForAddress", classOf[String], Integer.TYPE)
    method.setAccessible(true)
    val builder = method.invoke(provider, host, port).asInstanceOf[ManagedChannelBuilder[_]]
    builder.usePlaintext()
    val channel = builder.build()
    (channelType(channel), () => channel.shutdownNow())
  }
}

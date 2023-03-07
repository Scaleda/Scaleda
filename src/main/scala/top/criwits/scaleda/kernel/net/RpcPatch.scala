package top.criwits.scaleda
package kernel.net

import kernel.utils.KernelLogger

import io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader
import io.grpc._

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

  def getStartServer(services: => Seq[ServerServiceDefinition], port: Int): Server = {
    val provider = RpcPatch.getDefaultServerProvider
    val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
    method.setAccessible(true)
    val builder = method.invoke(provider, port).asInstanceOf[ServerBuilder[_]]
    services.foreach(service => builder.addService(service))
    KernelLogger.info("scaleda grpc server serve at port", port)
    val server = builder.build().start()
    sys.addShutdownHook {
      KernelLogger.info("gRPC server shutting down")
      server.shutdown()
      KernelLogger.info("gRPC server shutdown")
    }
    server
  }
}

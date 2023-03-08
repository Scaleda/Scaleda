package top.criwits.scaleda
package kernel.net

import kernel.net.user.{AuthorizationProvideCredentials, JwtAuthorizationInterceptor}
import kernel.utils.KernelLogger

import io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader
import io.grpc._
import io.grpc.stub.AbstractStub

import scala.language.existentials

object RpcPatch {
  private def getDefaultServerProvider =
    getCandidatesViaServiceLoader(classOf[ServerProvider], classOf[ServerProvider].getClassLoader).iterator.next
  private def getDefaultClientProvider = getCandidatesViaServiceLoader(
    classOf[ManagedChannelProvider],
    classOf[ManagedChannelProvider].getClassLoader
  ).iterator.next
  def getClient[T <: AbstractStub[T]](
      channelType: Channel => T,
      host: String,
      port: Int,
      enableAuthProvide: Boolean = false
  ) = {
    val provider = RpcPatch.getDefaultClientProvider
    val method   = provider.getClass.getDeclaredMethod("builderForAddress", classOf[String], Integer.TYPE)
    method.setAccessible(true)
    val builder = method.invoke(provider, host, port).asInstanceOf[ManagedChannelBuilder[_]]
    builder.usePlaintext()
    val channel = builder.build()
    var stub    = channelType(channel)
    if (enableAuthProvide) {
      stub = stub.withCallCredentials(new AuthorizationProvideCredentials)
    }
    (stub, () => channel.shutdownNow())
  }

  def getStartServer(
      services: => Seq[ServerServiceDefinition],
      port: Int,
      enableAuthProvide: Boolean = false
  ): Server = {
    val provider = RpcPatch.getDefaultServerProvider
    val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
    method.setAccessible(true)
    val builder = method.invoke(provider, port).asInstanceOf[ServerBuilder[_]]
    services.foreach(service => builder.addService(service))
    KernelLogger.info("scaleda grpc server serve at port", port)
    if (enableAuthProvide) {
      builder.intercept(new JwtAuthorizationInterceptor)
    }
    val server = builder.build().start()
    sys.addShutdownHook {
      KernelLogger.info("gRPC server shutting down")
      server.shutdown()
      KernelLogger.info("gRPC server shutdown")
    }
    server
  }
}

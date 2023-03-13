package top.criwits.scaleda
package kernel.net

import kernel.net.user.{AuthorizationProvideCredentials, JwtAuthorizationInterceptor}
import kernel.utils.KernelLogger

import io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader
import io.grpc._
import io.grpc.netty.shaded.io.grpc.netty.{NettyChannelBuilder, NettyServerBuilder}
import io.grpc.stub.AbstractStub

import java.net.InetSocketAddress
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
    // val channel =
    //       try {
    //         NettyChannelBuilder
    //           .forAddress(host, port)
    //           .enableRetry()
    //           .usePlaintext()
    //           .build()
    //       } catch {
    //         case ignored: NoSuchFieldError =>
    //           KernelLogger.info("using idea netty-grpc client")
    //           IdeaNettyChannelBuilder
    //             .forAddress(new InetSocketAddress(host, port))
    //             .build()
    //       }
    //     var stub = channelType(channel)
    val builder = NettyChannelBuilder
      .forAddress(host, port)
      .enableRetry()
      .usePlaintext()
    val channel = builder.build()
    var stub    = channelType(channel)
    if (enableAuthProvide) {
      stub = stub.withCallCredentials(new AuthorizationProvideCredentials)
    }
    (stub, () => channel.shutdownNow())
  }

  def getNativeClient[T <: AbstractStub[T]](
      channelType: Channel => T,
      host: String,
      port: Int,
      enableAuthProvide: Boolean = false
  ) = {
    val builder = ManagedChannelBuilder.forAddress(host, port)
    builder.usePlaintext()
    val channel = builder.build()
    var stub    = channelType(channel)
    if (enableAuthProvide) {
      stub = stub.withCallCredentials(new AuthorizationProvideCredentials)
    }
    stub
  }

  def getStartServer(
      services: => Seq[ServerServiceDefinition],
      port: Int,
      enableAuthCheck: Boolean = false
  ): Server = {
    val builder = NettyServerBuilder.forAddress(new InetSocketAddress("0.0.0.0", port))
    services.foreach(service => builder.addService(service))
    KernelLogger.info("scaleda grpc server serve at port", port)
    if (enableAuthCheck) {
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

package top.scaleda
package idea.utils

import kernel.net.user.{AuthorizationProvideCredentials, JwtAuthorizationInterceptor}
import kernel.utils.KernelLogger

import io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader
import io.grpc._
// import io.grpc.netty.shaded.io.grpc.netty.{NettyChannelBuilder, NettyServerBuilder}
// import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
// import io.grpc.netty.{NettyChannelBuilder, NettyServerBuilder}
import io.grpc.stub.AbstractStub

import java.net.InetSocketAddress
import scala.language.existentials

object IdeaRpcPatch {
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
    val curClassLoader = Thread.currentThread.getContextClassLoader
    try {
      curClassLoader.loadClass("io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder")
      import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
      val builder = NettyChannelBuilder
        .forAddress(host, port)
        .enableRetry()
        .usePlaintext()
      val channel = builder.build()
      var stub = channelType(channel)
      if (enableAuthProvide) {
        stub = stub.withCallCredentials(new AuthorizationProvideCredentials(host = host))
      }
      (stub, () => channel.shutdownNow())
    } catch {
      case e: Throwable => {
        throw new RuntimeException(s"current loader: ${curClassLoader}:" + e.getMessage)
      }
    } finally Thread.currentThread.setContextClassLoader(curClassLoader)
  }

  def getStartServer(
      services: => Seq[ServerServiceDefinition],
      port: Int,
      enableAuthCheck: Boolean = false
  ): Server = {
    import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
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

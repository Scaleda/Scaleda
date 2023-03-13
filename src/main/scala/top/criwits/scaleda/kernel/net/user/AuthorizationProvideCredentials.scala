package top.criwits.scaleda
package kernel.net.user

import kernel.utils.KernelLogger

import io.grpc.{CallCredentials, Metadata}

import java.util.concurrent.Executor

class AuthorizationProvideCredentials(host: String) extends CallCredentials {
  override def applyRequestMetadata(
      requestInfo: CallCredentials.RequestInfo,
      appExecutor: Executor,
      applier: CallCredentials.MetadataApplier
  ): Unit = {
    val header    = new Metadata()
    val tokenPair = ScaledaAuthorizationProvider.loadByHost(host)
    tokenPair.foreach(t => header.put(JwtAuthorizationInterceptor.AUTHORIZATION_META_KEY, t.token))
    if (tokenPair.nonEmpty) {
      KernelLogger.info("applyRequestMetadata", tokenPair)
    } else {
      KernelLogger.warn("No token loaded")
    }
    applier.apply(header)
  }

  override def thisUsesUnstableApi(): Unit = {}
}

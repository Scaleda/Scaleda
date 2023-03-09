package top.criwits.scaleda
package kernel.net.user

import kernel.utils.KernelLogger

import io.grpc.{CallCredentials, Metadata}

import java.util.concurrent.Executor

class AuthorizationProvideCredentials extends CallCredentials {
  override def applyRequestMetadata(
      requestInfo: CallCredentials.RequestInfo,
      appExecutor: Executor,
      applier: CallCredentials.MetadataApplier
  ): Unit = {
    val header    = new Metadata()
    val tokenPair = ScaledaAuthorizationProvider.loadTokenPair
    header.put(JwtAuthorizationInterceptor.AUTHORIZATION_META_KEY, tokenPair.token)
    KernelLogger.info("applyRequestMetadata", tokenPair)
    applier.apply(header)
  }

  override def thisUsesUnstableApi(): Unit = {}
}

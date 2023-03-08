package top.criwits.scaleda
package kernel.net.user

import kernel.database.ScaledaDatabase

import io.grpc.{CallCredentials, Metadata, Status}
import top.criwits.scaleda.kernel.utils.KernelLogger

import java.util.concurrent.Executor

class AuthorizationProvideCredentials extends CallCredentials {
  override def applyRequestMetadata(
      requestInfo: CallCredentials.RequestInfo,
      appExecutor: Executor,
      applier: CallCredentials.MetadataApplier
  ): Unit = {
    KernelLogger.info("applyRequestMetadata")
    // requestInfo.getAuthority
    val header = new Metadata()
    header.put(JwtAuthorizationInterceptor.AUTHORIZATION_META_KEY, "test")
    applier.apply(header)
  }

  override def thisUsesUnstableApi(): Unit = {}
}

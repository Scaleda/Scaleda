package top.criwits.scaleda
package kernel.net.user

import kernel.database.ScaledaDatabase

import io.grpc.{CallCredentials, Status}

import java.util.concurrent.Executor

class AuthorizationCheckCredentials extends CallCredentials {
  override def applyRequestMetadata(
      requestInfo: CallCredentials.RequestInfo,
      appExecutor: Executor,
      applier: CallCredentials.MetadataApplier
  ): Unit = {
    val auth       = requestInfo.getAuthority
    val token      = auth
    val db         = new ScaledaDatabase
    val foundToken = db.findToken(token)
    if (foundToken.isEmpty) {
      applier.fail(Status.UNAUTHENTICATED)
    }
  }

  override def thisUsesUnstableApi(): Unit = {}
}

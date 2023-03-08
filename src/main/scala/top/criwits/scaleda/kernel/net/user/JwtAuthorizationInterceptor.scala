package top.criwits.scaleda
package kernel.net.user

import kernel.database.ScaledaDatabase
import kernel.database.dao.Token
import kernel.utils.KernelLogger

import com.auth0.jwt.exceptions.JWTVerificationException
import io.grpc.Metadata.ASCII_STRING_MARSHALLER
import io.grpc._

import scala.collection.immutable.HashSet

class JwtAuthorizationInterceptor(passServices: HashSet[String] = HashSet[String]("RemoteRegisterLogin"))
    extends ServerInterceptor {
  override def interceptCall[ReqT, RespT](
      serverCall: ServerCall[ReqT, RespT],
      metadata: Metadata,
      next: ServerCallHandler[ReqT, RespT]
  ): ServerCall.Listener[ReqT] = {
    KernelLogger.debug("metadata keys", metadata.keys())
    val m           = serverCall.getMethodDescriptor
    val serviceName = m.getServiceName.split('.').last
    KernelLogger.debug("serviceName", serviceName)
    if (passServices.contains(serviceName)) return Contexts.interceptCall(Context.current(), serverCall, metadata, next)
    val value = metadata.get(JwtAuthorizationInterceptor.AUTHORIZATION_META_KEY)

    var status = Status.OK
    if (value == null) status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing")
    else {
      var claims: Option[Token] = None
      val token                 = value
      try {
        val db    = new ScaledaDatabase
        val found = db.findToken(token)
        if (found.isEmpty) {
          throw new JWTVerificationException("Invalid token")
        }
        claims = found
      } catch {
        case e: JWTVerificationException =>
          status = Status.UNAUTHENTICATED.withDescription(e.getMessage).withCause(e)
      }
      if (claims.isDefined) {
        // set client id into current context
        val ctx = Context.current.withValue(JwtAuthorizationInterceptor.USERNAME_CONTEXT_KEY, claims.get)
        return Contexts.interceptCall(ctx, serverCall, metadata, next)
      }
    }

    serverCall.close(status, new Metadata())
    new ServerCall.Listener[ReqT]() {}
  }
}

object JwtAuthorizationInterceptor {
  final val AUTHORIZATION_META_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER)
  final val USERNAME_CONTEXT_KEY   = Context.key[Token]("username")
}

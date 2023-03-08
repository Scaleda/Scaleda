package top.criwits.scaleda
package kernel.net.user

import kernel.database.ScaledaDatabase
import kernel.database.dao.Token
import kernel.utils.KernelLogger

import com.auth0.jwt.exceptions.JWTVerificationException
import io.grpc.Metadata.ASCII_STRING_MARSHALLER
import io.grpc._

class JwtAuthorizationInterceptor extends ServerInterceptor {
  override def interceptCall[ReqT, RespT](
      serverCall: ServerCall[ReqT, RespT],
      metadata: Metadata,
      next: ServerCallHandler[ReqT, RespT]
  ): ServerCall.Listener[ReqT] = {
    KernelLogger.info("metadata keys", metadata.keys())
    val value = metadata.get(JwtAuthorizationInterceptor.AUTHORIZATION_META_KEY)

    var status = Status.OK
    if (value == null) status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing")
    // else if (!value.startsWith(Constant.BEARER_TYPE)) status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type")
    else {
      var claims: Option[Token] = None
      // remove authorization type prefix
      val token = value
      try {
        // verify token signature and parse claims
        // claims = parser.parseClaimsJws(token)
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
    new ServerCall.Listener[ReqT]() {
      // noop
    }
  }
}

object JwtAuthorizationInterceptor {
  final val AUTHORIZATION_META_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER)
  final val USERNAME_CONTEXT_KEY   = Context.key[Token]("username")
}

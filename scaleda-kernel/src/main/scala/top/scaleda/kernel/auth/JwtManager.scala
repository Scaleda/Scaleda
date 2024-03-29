package top.scaleda
package kernel.auth

import kernel.utils.{EnvironmentUtils, KernelLogger}

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.{JWTCreationException, JWTVerificationException}
import com.auth0.jwt.interfaces.DecodedJWT

import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.security.{KeyFactory, KeyPairGenerator, SecureRandom}
import java.time.Duration
import java.util.{Base64, Date}

object JwtManager {
  val publicKey  = EnvironmentUtils.Backup.env.get("JWT_RSA_PUBLIC_KEY")
  val privateKey = EnvironmentUtils.Backup.env.get("JWT_RSA_PRIVATE_KEY")
  if (publicKey.isEmpty || privateKey.isEmpty) KernelLogger.warn("JWT_RSA_* is empty! RSA Key Pair required for JWT")
  val rsaPublicKey =
    publicKey.map(key =>
      KeyFactory
        .getInstance("RSA")
        .generatePublic(new X509EncodedKeySpec(Base64.getDecoder.decode(key)))
        .asInstanceOf[RSAPublicKey]
    )
  val rsaPrivateKey = privateKey.map(key =>
    KeyFactory
      .getInstance("RSA")
      .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder.decode(key)))
      .asInstanceOf[RSAPrivateKey]
  )

  def create(validTime: Duration, claims: Map[String, String] = Map()): Option[String] = {
    if (rsaPrivateKey.isEmpty || rsaPublicKey.isEmpty) return None
    val now = new Date()
    try {
      val algorithm = Algorithm.RSA256(rsaPublicKey.get, rsaPrivateKey.get)
      var builder = JWT.create
        .withIssuer("auth0")
        .withExpiresAt(new Date(now.getTime + validTime.toMillis).toInstant)
      for ((k, v) <- claims) builder = builder.withClaim(k, v)
      builder = builder.withClaim("random", Integer.valueOf(new SecureRandom().nextInt()))
      val token = builder.sign(algorithm)
      KernelLogger.debug("create token (hash):", token.hashCode)
      Some(token)
    } catch {
      case e: JWTCreationException =>
        // Invalid Signing configuration / Couldn't convert Claims.
        KernelLogger.error(e)
        None
    }
  }

  def createToken(validTime: Duration = Duration.ofDays(3), claims: Map[String, String] = Map()) =
    create(validTime, claims)
  def createRefreshToken(validTime: Duration = Duration.ofDays(30), claims: Map[String, String] = Map()) =
    create(validTime, claims)

  def decode(token: String): Option[DecodedJWT] = {
    if (rsaPrivateKey.isEmpty || rsaPublicKey.isEmpty) return None
    try {
      val algorithm = Algorithm.RSA256(rsaPublicKey.get, rsaPrivateKey.get)
      val verifier  = JWT.require(algorithm).withIssuer("auth0").build
      Some(verifier.verify(token))
    } catch {
      case e: JWTVerificationException =>
        // Invalid signature/claims
        println(e)
        None
    }
  }
}

package top.criwits.scaleda
package kernel.auth

import kernel.utils.{EnvironmentUtils, KernelLogger}

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.{JWTCreationException, JWTVerificationException}
import com.auth0.jwt.interfaces.DecodedJWT

import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.security.{KeyFactory, KeyPairGenerator, SecureRandom}
import java.util.Base64

object JwtManager {
  // TODO: set private key in env
  val publicKey  = EnvironmentUtils.Backup.env("JWT_RSA_PUBLIC_KEY")
  val privateKey = EnvironmentUtils.Backup.env("JWT_RSA_PRIVATE_KEY")
  val rsaPublicKey =
    KeyFactory
      .getInstance("RSA")
      .generatePublic(new X509EncodedKeySpec(Base64.getDecoder.decode(publicKey)))
      .asInstanceOf[RSAPublicKey]
  val rsaPrivateKey =
    KeyFactory
      .getInstance("RSA")
      .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder.decode(privateKey)))
      .asInstanceOf[RSAPrivateKey]

  def create(): Option[String] = {
    try {
      val algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey)
      val token     = JWT.create.withIssuer("auth0").sign(algorithm)
      Some(token)
    } catch {
      case exception: JWTCreationException =>
        // Invalid Signing configuration / Couldn't convert Claims.
        None
    }
  }
  def decode(token: String): Option[DecodedJWT] = {
    try {
      val algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey)
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

object JwtTest extends App {
  for ((k, v) <- sys.env) {
    KernelLogger.info(s"[$k]: $v")
  }
  val token = JwtManager.create().get
  KernelLogger.info(s"token = $token")
  val decoded = JwtManager.decode(token).get
  KernelLogger.info(s"decoded = ${decoded}")
  KernelLogger.info("header", decoded.getHeader, "payload", decoded.getPayload, "signature", decoded.getSignature)
  KernelLogger.info("exp", decoded.getExpiresAt)
}

object RSAKeyPairGenerate extends App {
  val gen = KeyPairGenerator.getInstance("RSA")
  gen.initialize(2048, new SecureRandom)
  val keyPair          = gen.generateKeyPair()
  val publicKey        = keyPair.getPublic
  val privateKey       = keyPair.getPrivate
  val publicKeyString  = new String(Base64.getEncoder.encode(publicKey.getEncoded))
  val privateKeyString = new String(Base64.getEncoder.encode(privateKey.getEncoded))
  println(s"publicKeyString = $publicKeyString")
  println(s"privateKeyString = $privateKeyString")
}

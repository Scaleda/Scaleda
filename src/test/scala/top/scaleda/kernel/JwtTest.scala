package top.scaleda
package kernel

import kernel.auth.JwtManager
import kernel.utils.KernelLogger

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.security.{KeyPairGenerator, SecureRandom}
import java.util.Base64

class JwtTest extends AnyFlatSpec with should.Matchers {
  behavior of "Jwt"
  it should "test jwt encode / decode" in {
    val token = JwtManager.createToken().get
    KernelLogger.info(s"token = $token")
    val decoded = JwtManager.decode(token).get
    KernelLogger.info(s"decoded = ${decoded}")
    KernelLogger.info("header", decoded.getHeader, "payload", decoded.getPayload, "signature", decoded.getSignature)
    KernelLogger.info("exp", decoded.getExpiresAt)
  }

  it should "generate and print key-pair" in {
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
}

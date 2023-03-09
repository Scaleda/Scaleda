package top.criwits.scaleda
package kernel.net.user

import kernel.utils.{EnvironmentUtils, Paths}
import kernel.utils.serialise.YAMLHelper

object ScaledaAuthorizationProvider {
  def loadTokenPair: TokenPair = {
    if (EnvironmentUtils.Backup.env.contains("AUTH_DEV_MODE")) {
      TokenPair.TEST_MODE_PAIR
    } else {
      val file = Paths.getUserAuthorization
      try {
        YAMLHelper(file, classOf[TokenPair])
      } catch {
        case e: Throwable => TokenPair()
      }
    }
  }

  def saveTokenPair(tokenPair: TokenPair): Unit = {
    val file = Paths.getUserAuthorization
    YAMLHelper(tokenPair, file)
  }

  def updateToken(token: String): Unit = {
    val tokenPair    = loadTokenPair
    val newTokenPair = tokenPair.copy(token = token)
    saveTokenPair(newTokenPair)
  }

  def updateRefreshToken(refreshToken: String): Unit = {
    val tokenPair    = loadTokenPair
    val newTokenPair = tokenPair.copy(refreshToken = refreshToken)
    saveTokenPair(newTokenPair)
  }
}

case class TokenPair(token: String = "", refreshToken: String = "")

object TokenPair {
  final val TEST_MODE_PAIR = TokenPair("token-test", "token-test")
}

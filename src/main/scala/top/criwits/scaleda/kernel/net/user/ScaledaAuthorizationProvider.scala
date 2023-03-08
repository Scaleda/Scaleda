package top.criwits.scaleda
package kernel.net.user

import kernel.utils.Paths
import kernel.utils.serialise.YAMLHelper

object ScaledaAuthorizationProvider {
  def loadTokenPair: TokenPair = {
    val file = Paths.getUserAuthorization
    try {
      YAMLHelper(file, classOf[TokenPair])
    } catch {
      case e: Throwable => TokenPair()
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
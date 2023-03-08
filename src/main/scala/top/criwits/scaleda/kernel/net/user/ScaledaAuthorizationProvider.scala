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
}

case class TokenPair(token: String = "", refreshToken: String = "")

package top.criwits.scaleda
package kernel.net.user

import kernel.utils.serialise.YAMLHelper
import kernel.utils.{EnvironmentUtils, Paths}

object ScaledaAuthorizationProvider {
  def loadTokenPair: UserTokenBean = {
    if (EnvironmentUtils.Backup.env.contains("AUTH_DEV_MODE")) {
      UserTokenBean.TEST_MODE_PAIR
    } else {
      val file = Paths.getUserAuthorization
      try {
        YAMLHelper(file, classOf[UserTokenBean])
      } catch {
        case e: Throwable => UserTokenBean()
      }
    }
  }

  def saveTokenPair(tokenPair: UserTokenBean): Unit = {
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

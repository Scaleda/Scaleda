package top.criwits.scaleda
package kernel.net.user

import kernel.utils.serialise.YAMLHelper
import kernel.utils.{EnvironmentUtils, Paths}

import scala.collection.mutable

object ScaledaAuthorizationProvider {
  def loadTokenPair: Map[String, UserTokenBean] = {
    val extras = if (EnvironmentUtils.Backup.env.contains("AUTH_DEV_MODE")) {
      Some("127.0.0.1" -> UserTokenBean.TEST_MODE_PAIR)
    } else None
    val file     = Paths.getUserAuthorization
    var hasError = false
    val loaded =
      try {
        YAMLHelper(file, classOf[Map[String, UserTokenBean]])
      } catch {
        case e: Throwable =>
          hasError = true
          Map[String, UserTokenBean]()
      }
    if (hasError) saveTokenPairs(loaded)
    if (extras.nonEmpty) loaded + extras.get
    else loaded
  }

  def saveTokenPair(host: String, tokenPair: UserTokenBean): Unit = {
    val file     = Paths.getUserAuthorization
    val original = new mutable.HashMap[String, UserTokenBean]()
    original.addAll(loadTokenPair.filter(_._2 != UserTokenBean.TEST_MODE_PAIR))
    original.put(host, tokenPair)
    YAMLHelper(original.toMap, file)
  }

  def saveTokenPairs(tokenPairs: Map[String, UserTokenBean]): Unit = {
    val file = Paths.getUserAuthorization
    YAMLHelper(tokenPairs, file)
  }

  def updateToken(host: String, token: String): Unit = {
    val tokenPair    = loadTokenPair.getOrElse(host, UserTokenBean())
    val newTokenPair = tokenPair.copy(token = token)
    saveTokenPair(host, newTokenPair)
  }

  def updateRefreshToken(host: String, refreshToken: String): Unit = {
    val tokenPair    = loadTokenPair.getOrElse(host, UserTokenBean(token = refreshToken))
    val newTokenPair = tokenPair.copy(refreshToken = refreshToken)
    saveTokenPair(host, newTokenPair)
  }

  def updateUsername(host: String, username: String): Unit = {
    val tokenPair    = loadTokenPair.getOrElse(host, UserTokenBean())
    val newTokenPair = tokenPair.copy(username = username)
    saveTokenPair(host, newTokenPair)
  }
}

package top.criwits.scaleda
package kernel.net.user

import kernel.utils.serialise.YAMLHelper
import kernel.utils.{EnvironmentUtils, KernelLogger, Paths}

object ScaledaAuthorizationProvider {
  def loadTokenPair: Seq[UserTokenBean] = {
    val extras = if (EnvironmentUtils.Backup.env.contains("AUTH_DEV_MODE")) {
      Some(UserTokenBean.TEST_MODE_PAIR)
    } else None
    val file     = Paths.getUserAuthorization
    var hasError = false
    val loaded =
      try {
        val loaded = YAMLHelper(file, classOf[Seq[Map[String, String]]])
        require(loaded.isInstanceOf[Seq[Map[String, String]]])
        loaded.map(UserTokenBean.fromMap)
      } catch {
        case e: Throwable =>
          KernelLogger.warn("Error when loading authorizations", e, "save default file")
          hasError = true
          Seq()
      }
    if (hasError) saveTokenPairs(loaded)
    val result =
      if (extras.nonEmpty) loaded :+ extras.get
      else loaded
    require(result.isInstanceOf[Seq[UserTokenBean]])
    result.headOption.foreach(t => require(t.isInstanceOf[UserTokenBean]))
    result
  }

  def loadByHost(host: String) = loadTokenPair.find(_.host == host)

  def saveTokenPair(tokenPair: UserTokenBean): Unit = {
    val loaded = loadTokenPair.filter(!_.host.equals(tokenPair.host))
    val appended =
      loaded
        .filter(p => !p.equals(UserTokenBean.TEST_MODE_PAIR)) :+
        tokenPair
    YAMLHelper(appended, Paths.getUserAuthorization)
  }

  def saveTokenPairs(tokenPairs: Seq[UserTokenBean]): Unit = {
    val file      = Paths.getUserAuthorization
    val hostNames = tokenPairs.map(_.host).toSet
    val data: Seq[Map[String, String]] =
      hostNames.map(host => tokenPairs.find(_.host == host).get.toMap).toSeq
    YAMLHelper(data, file)
  }

  private def getPairByHost(host: String) = loadByHost(host).getOrElse(UserTokenBean(host = host))

  def updateToken(host: String, token: String): Unit = {
    val tokenPair    = getPairByHost(host)
    val newTokenPair = tokenPair.copy(token = token)
    saveTokenPair(newTokenPair)
  }

  def updateRefreshToken(host: String, refreshToken: String): Unit = {
    val tokenPair    = getPairByHost(host).copy(token = refreshToken)
    val newTokenPair = tokenPair.copy(refreshToken = refreshToken)
    saveTokenPair(newTokenPair)
  }

  def updateUsername(host: String, username: String): Unit = {
    val tokenPair    = getPairByHost(host)
    val newTokenPair = tokenPair.copy(username = username)
    saveTokenPair(newTokenPair)
  }
}

package top.scaleda
package kernel.net.user

case class UserTokenBean(
    host: String,
    username: String = "",
    token: String = "",
    refreshToken: String = ""
) {
  def toMap: Map[String, String] = Map(
    "host"         -> host,
    "username"     -> username,
    "token"        -> token,
    "refreshToken" -> refreshToken
  )
}

object UserTokenBean {
  final val TEST_MODE_PAIR =
    UserTokenBean(host = "127.0.0.1", username = "test", token = "token-test", refreshToken = "token-test")

  def fromMap(m: Map[String, String]): UserTokenBean = UserTokenBean(
    host = m.getOrElse("host", ""),
    username = m.getOrElse("username", ""),
    token = m.getOrElse("token", ""),
    refreshToken = m.getOrElse("refreshToken", "")
  )
}

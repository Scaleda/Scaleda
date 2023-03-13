package top.criwits.scaleda
package kernel.net.user

case class UserTokenBean(
    host: String,
    username: String = "",
    token: String = "",
    refreshToken: String = ""
)

object UserTokenBean {
  final val TEST_MODE_PAIR =
    UserTokenBean(host = "127.0.0.1", username = "test", token = "token-test", refreshToken = "token-test")
}

package top.criwits.scaleda
package kernel.net.user

case class UserTokenBean(username: String = "", token: String = "", refreshToken: String = "")

object UserTokenBean {
  final val TEST_MODE_PAIR = UserTokenBean("test", "token-test", "token-test")
}

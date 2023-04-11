package top.criwits.scaleda
package kernel.net.user

import kernel.auth.AuthorizationHasher
import kernel.database.dao.User
import kernel.net.remote._
import kernel.net.{RemoteServer, RpcPatch}

class ScaledaRegisterLogin(host: String, port: Int = RemoteServer.DEFAULT_PORT) {
  // TODO: timeout option if server dies
  private def getClient = RpcPatch.getClient(RemoteRegisterLoginGrpc.blockingStub, host, port)

  def register(user: User): RemoteRegisterReply = {
    user.setPassword(AuthorizationHasher.encodeString(user.getPassword))
    val (client, shutdown) = getClient
    val reply              = client.register(RemoteRegisterRequest.of(user.getUsername, user.getPassword, user.getNickname))
    shutdown()
    reply
  }
  def login(username: String, password: String, save: Boolean = true): RemoteLoginReply = {
    val passwordHashed     = AuthorizationHasher.encodeString(password)
    val (client, shutdown) = getClient
    val reply              = client.login(RemoteLoginRequest.of(username, passwordHashed))
    shutdown()
    if (reply.ok && save) {
      ScaledaAuthorizationProvider.saveTokenPair(
        UserTokenBean(host = host, username = username, token = reply.token, refreshToken = reply.refreshToken)
      )
    }
    reply
  }
  def refreshAndStore(): Boolean = {
    val tokenPair = ScaledaAuthorizationProvider.loadByHost(host)
    tokenPair.exists(t => {
      val (client, shutdown) = getClient
      val reply              = client.refresh(RemoteRefreshRequest.of(t.refreshToken))
      shutdown()
      if (reply.ok) {
        ScaledaAuthorizationProvider.updateToken(host, reply.token)
      }
      reply.ok
    })
  }
}

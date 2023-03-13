package top.criwits.scaleda
package kernel.net.user

import kernel.database.dao.User
import kernel.net.remote._
import kernel.net.{RemoteServer, RpcPatch}

class ScaledaRegisterLogin(host: String, port: Int = RemoteServer.DEFAULT_PORT) {
  // TODO: timeout option if server dies
  private def getClient = RpcPatch.getClient(RemoteRegisterLoginGrpc.blockingStub, host, port)

  def register(user: User): RemoteRegisterReply = {
    val (client, shutdown) = getClient
    val reply              = client.register(RemoteRegisterRequest.of(user.getUsername, user.getPassword, user.getNickname))
    shutdown()
    reply
  }
  def login(username: String, password: String): RemoteLoginReply = {
    val (client, shutdown) = getClient
    val reply              = client.login(RemoteLoginRequest.of(username, password))
    shutdown()
    if (reply.ok) {
      ScaledaAuthorizationProvider.saveTokenPair(
        host,
        UserTokenBean(username = username, token = reply.token, refreshToken = reply.refreshToken)
      )
    }
    reply
  }
  def refreshAndStore(): Boolean = {
    val tokenPair = ScaledaAuthorizationProvider.loadTokenPair.get(host)
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

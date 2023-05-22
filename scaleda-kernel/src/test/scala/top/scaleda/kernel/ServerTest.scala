package top.scaleda
package kernel

import kernel.database.ScaledaDatabase
import kernel.database.dao.User
import kernel.net.remote._
import kernel.net.user.ScaledaRegisterLogin
import kernel.net.{RemoteClient, RemoteServer, RpcPatch}
import kernel.server.ScaledaServerMain
import kernel.shell.ShellArgs
import kernel.utils.KernelLogger

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

object ScaledaServerMainRunTest {
  def run(): Unit = {
    new ScaledaDatabase().forceCleanDatabase()
    ScaledaServerMain.run(ShellArgs())
  }
}

class ServerTest extends AnyFlatSpec with should.Matchers {
  behavior of "Server"

  it should "launch server and clean database" in {
    ScaledaServerMainRunTest.run()
  }

  it should "do server-client functional test" in {
    // val thread = new Thread(() => ScaledaServerMain.run(ShellArgs()))
    val thread = new Thread(() => {
      try {
        RemoteServer.serve()
      } catch {
        case _e: InterruptedException => {}
      }
    })
    thread.start()

    val db = new ScaledaDatabase
    db.forceCleanDatabase()

    {
      val (client, shutdown) =
        RpcPatch.getClient(
          RemoteRegisterLoginGrpc.blockingStub,
          "127.0.0.1",
          RemoteServer.DEFAULT_PORT,
          enableAuthProvide = true
        )
      val registerReply = client.register(RemoteRegisterRequest.of("test", "test", ""))
      val loginReply    = client.login(RemoteLoginRequest.of("test", "test"))
      val refreshToken  = loginReply.refreshToken
      val token         = loginReply.token
      val refreshReply  = client.refresh(RemoteRefreshRequest.of(refreshToken))
      val tokenNew      = refreshReply.token
      KernelLogger.info("refresh token", refreshToken, "token", token, "new token", tokenNew)
      shutdown()
    }
    {
      val username = "user"
      val password = "pass"
      val req      = new ScaledaRegisterLogin("127.0.0.1")
      req.register(new User(username, password, username))
      req.login(username, password)

      // val (client, shutdown) = RemoteClient("127.0.0.1")
      // val reply              = client.getProfiles(top.scaleda.kernel.net.remote.Empty.of())
      // KernelLogger.info("remote profiles", reply.profiles)
      // shutdown()

      // val client = RpcPatch.getNativeClient(
      //   RemoteGrpc.blockingStub,
      //   "127.0.0.1",
      //   RemoteServer.DEFAULT_PORT,
      //   enableAuthProvide = true
      // )
      val (client, shutdown)           = RemoteClient("127.0.0.1")
      val reply                        = client.getProfiles(top.scaleda.kernel.net.remote.Empty.of())
      val profiles: Seq[RemoteProfile] = reply.profiles
      KernelLogger.info("remote profiles", profiles)
      shutdown()
    }

    thread.interrupt()
    KernelLogger.info("main done")
    // System.exit(0)
    // thread.join()

  }
}

package top.criwits.scaleda
package kernel.server

import kernel.database.ScaledaDatabase
import kernel.database.dao.User
import kernel.net.fuse.FuseTransferServer
import kernel.net.remote._
import kernel.net.user.ScaledaRegisterLogin
import kernel.net.{RemoteClient, RemoteServer, RpcPatch}
import kernel.shell.ShellArgs
import kernel.utils.KernelLogger

import scala.collection.mutable.ArrayBuffer

object ScaledaServerMain {
  def run(shellConfig: ShellArgs): Unit = {
    val threads = ArrayBuffer[Thread]()
    val threadRpcServer = new Thread(() => {
      RemoteServer.serve()
    })
    threadRpcServer.start()
    threads.addOne(threadRpcServer)
    val sendFsRpcMessageThread = FuseTransferServer.requestThread
    sendFsRpcMessageThread.start()
    threads.addOne(sendFsRpcMessageThread)
    var loop = true
    while (loop) {
      try {
        threads.foreach(_.join())
      } catch {
        case _: InterruptedException => loop = false
      }
    }
    KernelLogger.info("all server threads exited")
  }
}

object ScaledaServerMainRunTest extends App {
  ScaledaServerMain.run(ShellArgs())
}

object ScaledaServerMainTest extends App {
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
    // val reply              = client.getProfiles(top.criwits.scaleda.kernel.net.remote.Empty.of())
    // KernelLogger.info("remote profiles", reply.profiles)
    // shutdown()

    // val client = RpcPatch.getNativeClient(
    //   RemoteGrpc.blockingStub,
    //   "127.0.0.1",
    //   RemoteServer.DEFAULT_PORT,
    //   enableAuthProvide = true
    // )
    val (client, shutdown)           = RemoteClient("127.0.0.1")
    val reply                        = client.getProfiles(top.criwits.scaleda.kernel.net.remote.Empty.of())
    val profiles: Seq[RemoteProfile] = reply.profiles
    KernelLogger.info("remote profiles", profiles)
    shutdown()
  }

  thread.interrupt()
  KernelLogger.info("main done")
  // System.exit(0)
  // thread.join()
}

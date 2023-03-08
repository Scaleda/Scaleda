package top.criwits.scaleda
package kernel.server

import kernel.database.ScaledaDatabase
import kernel.net.remote.{RemoteLoginRequest, RemoteRefreshRequest, RemoteRegisterLoginGrpc, RemoteRegisterRequest}
import kernel.net.{RemoteServer, RpcPatch}
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

object ScaledaServerMainTest extends App {
  val thread = new Thread(() => ScaledaServerMain.run(ShellArgs()))
  thread.start()

  val db = new ScaledaDatabase
  db.forceCleanDatabase()

  val (client, shutdown) =
    RpcPatch.getClient(RemoteRegisterLoginGrpc.blockingStub, "127.0.0.1", RemoteServer.DEFAULT_PORT)
  val registerReply = client.register(RemoteRegisterRequest.of("test", "test", ""))
  val loginReply    = client.login(RemoteLoginRequest.of("test", "test"))
  val refreshToken  = loginReply.refreshToken
  val token         = loginReply.token
  val refreshReply  = client.refresh(RemoteRefreshRequest.of(refreshToken))
  val tokenNew      = refreshReply.token

  KernelLogger.info("refresh token", refreshToken, "token", token, "new token", tokenNew)

  thread.interrupt()
  KernelLogger.info("main done")
  System.exit(0)
  thread.join()
}

package top.criwits.scaleda
package kernel.server

import kernel.net.RemoteServer
import kernel.net.fuse.FuseTransferServer
import kernel.shell.ShellArgs
import kernel.utils.KernelLogger

import scala.collection.mutable.ArrayBuffer

object ScaledaServerMain {
  def run(shellConfig: ShellArgs): Unit = {
    val threads = ArrayBuffer[Thread]()
    val threadRpcServer = new Thread(
      () => {
        RemoteServer.serve()
      },
      "server-main-rpc-server-thread"
    )
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

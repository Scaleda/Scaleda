package top.criwits.scaleda
package kernel.server

import kernel.net.RemoteServer
import kernel.shell.ShellArgs

import scala.collection.mutable.ArrayBuffer

object ScaledaServerMain {
  def run(shellConfig: ShellArgs): Unit = {
    val threads = ArrayBuffer[Thread]()
    val threadRpcServer = new Thread(() => {
      RemoteServer.serve()
    })
    threadRpcServer.start()
    threads.addOne(threadRpcServer)
    threads.foreach(_.join())
  }
}

object ScaledaServerMainTest extends App {
  ScaledaServerMain.run(ShellArgs())
}

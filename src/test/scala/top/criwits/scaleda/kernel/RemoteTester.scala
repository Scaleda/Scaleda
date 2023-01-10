package top.criwits.scaleda
package kernel

import kernel.net.RemoteServer
import kernel.shell.ScaledaRunKernelHandler
import kernel.shell.command.{CommandDeps, RemoteCommandDeps, RemoteCommandRunner}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class RemoteTester extends AnyFlatSpec with should.Matchers {
  behavior of "CommandRunner"

  it should "test remote command execution" in {
    val serverThread = new Thread(() => RemoteServer.start())
    serverThread.setDaemon(true)
    serverThread.start()
    val remoteCommandDeps = RemoteCommandDeps()
    val commands = Seq(CommandDeps("ping -c 3 127.0.0.1"), CommandDeps("echo hi")) // FIXME: remote OS type get?
    RemoteCommandRunner.execute(remoteCommandDeps, commands, ScaledaRunKernelHandler)
  }
}

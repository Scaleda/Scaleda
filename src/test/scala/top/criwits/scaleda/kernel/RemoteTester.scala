package top.criwits.scaleda
package kernel

import kernel.net.RemoteServer
import kernel.net.remote.{Empty, RemoteGrpc}
import kernel.shell.ScaledaRunKernelHandler
import kernel.shell.command.{CommandDeps, RemoteCommandDeps, RemoteCommandRunner}

import io.grpc.{ManagedChannelBuilder, StatusRuntimeException}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.language.existentials

class RemoteTester extends AnyFlatSpec with should.Matchers {
  behavior of "CommandRunner"

  def startServer(): Thread = {
    val serverThread = new Thread(() => RemoteServer.start())
    serverThread.setDaemon(true)
    serverThread.start()
    serverThread
  }

  def getStub(host: String = "localhost", port: Int = RemoteServer.port) = {
    val builder =
      ManagedChannelBuilder.forAddress(host, port)
    builder.usePlaintext()
    val channel = builder.build()
    val stub = RemoteGrpc.blockingStub(channel)
    stub
  }

  it should "test remote command execution" in {
    val t = startServer()
    val remoteCommandDeps = RemoteCommandDeps()
    val commands = Seq(
      CommandDeps("ping -c 3 127.0.0.1"),
      CommandDeps("echo hi")
    ) // FIXME: remote OS type get?
    RemoteCommandRunner.execute(
      remoteCommandDeps,
      commands,
      ScaledaRunKernelHandler
    )
    t.interrupt()
  }

  it should "call invalid remote server" in {
    val t = startServer()
    // val stub = getStub("20.11.114.51")
    val stub = getStub(port = 1)
    try {
      val _ = stub.getProfiles(Empty())
    } catch {
      case e: StatusRuntimeException => println("visit invalid host")
    } finally {
      println("done")
    }
    t.interrupt()
  }
}

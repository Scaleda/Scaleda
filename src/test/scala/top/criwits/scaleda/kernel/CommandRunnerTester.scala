package top.criwits.scaleda
package kernel

import kernel.shell.ScaledaRunKernelHandler
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.utils.{KernelLogger, OS}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CommandRunnerTester extends AnyFlatSpec with should.Matchers {
  behavior of "CommandRunner"

  it should "echo `helloworld`" in {
    val command = OS.shell("echo helloworld")
    KernelLogger.info(s"Test command: $command")

    val cmd = CommandDeps(command)
    val runner = new CommandRunner(cmd)
    var output: String = ""

    val r = runner.run
    while(!r.returnValue.isCompleted) {} // Wait until completed
    r.stdOut.forEach(s => output += s)

    assert(output == "helloworld")
  }

  it should "ping 127.0.0.1" in {
    val ping = CommandDeps(OS.shell(s"ping ${if (OS.isWindows) "-n" else "-c"} 4 127.0.0.1"))
    CommandRunner.execute(Seq(ping), ScaledaRunKernelHandler)
  }
}

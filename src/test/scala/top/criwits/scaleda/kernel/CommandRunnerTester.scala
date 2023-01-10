package top.criwits.scaleda
package kernel

import kernel.shell.ScaledaRunKernelHandler
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.utils.{KernelLogger, OS}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CommandRunnerTester extends AnyFlatSpec with should.Matchers {
  behavior of "CommandRunner"

  it should "simply echo `helloworld`" in {
    val shell = if (OS.getOSType == OS.Windows) "C:\\Windows\\System32\\cmd.exe /c" else "/bin/sh -c"
    val command = s"$shell \"echo helloworld\""
    KernelLogger.info(s"Test command: $command")

    val cmd = CommandDeps(command)
    val runner = new CommandRunner(cmd)
    var output: String = ""

    val r = runner.run
    while(!r.returnValue.isCompleted) {} // Wait until completed
    r.stdOut.forEach(s => output += s)

    assert(output == "helloworld")
  }

  it should "test ping output" in {
    val ping = CommandDeps("ping -c 3 127.0.0.1")

    {
      val runner = new CommandRunner(ping)
      val r = runner.run
      while (!r.returnValue.isCompleted) {
        r.stdOut.forEach(s => KernelLogger.info(s))
        r.stdErr.forEach(s => KernelLogger.error(s))
        Thread.sleep(CommandRunner.delay)
      }
      r.stdOut.forEach(s => KernelLogger.info(s))
      r.stdErr.forEach(s => KernelLogger.error(s))
      KernelLogger.info(s"return value: ${r.returnValue.value.get}")
    }
    {
      val commands = Seq(ping, CommandDeps("echo hi"),
        // CommandDeps("/opt/Xilinx/Vivado/2019.2/bin/vivado -help")
      )
      CommandRunner.execute(commands, ScaledaRunKernelHandler)
    }
  }
}

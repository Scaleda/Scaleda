package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.shell.command.{CommandDeps, CommandRunner}
import top.criwits.scaleda.kernel.utils.{KernelLogger, OS}

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
}

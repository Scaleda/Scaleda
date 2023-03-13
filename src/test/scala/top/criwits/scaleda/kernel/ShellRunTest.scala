package top.criwits.scaleda
package kernel

import kernel.shell.ScaledaShellMain
import kernel.utils.EnvironmentUtils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ShellRunTest extends AnyFlatSpec with should.Matchers {
  behavior of "ShellRun"
  it should "test vivado impl" in {
    EnvironmentUtils.Backup.env.put("SKIP_EXECUTION", "1")
    ScaledaShellMain.main(Array("run", "-C", "../scaleda-sample-project", "-t", "Vivado Implementation"))
  }
}

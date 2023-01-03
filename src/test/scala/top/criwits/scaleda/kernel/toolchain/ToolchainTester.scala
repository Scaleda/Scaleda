package top.criwits.scaleda
package kernel.toolchain


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.toolchain.impl.{Verilator, Vivado}

class ToolchainTester extends AnyFlatSpec with should.Matchers {
  behavior of "Toolchains"
  it should "be loaded from YAML files" in {
  }
}
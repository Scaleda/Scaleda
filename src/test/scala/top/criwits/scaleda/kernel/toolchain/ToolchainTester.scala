package top.criwits.scaleda
package kernel.toolchain


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ToolchainTester extends AnyFlatSpec with should.Matchers {
  behavior of "Toolchains"
  it should "be loaded from YAML files" in {
    Toolchain.loadToolchainProfiles()
  }
}
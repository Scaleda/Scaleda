package top.criwits.scaleda
package kernel.toolchain


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.project.config.ProjectConfig
import top.criwits.scaleda.kernel.toolchain.impl.{Verilator, Vivado}
import top.criwits.scaleda.kernel.utils.{JsonHelper, KernelLogger}

class ToolchainTester extends AnyFlatSpec with should.Matchers {
  val testPath = "../scaleda-sample-project"

  behavior of "Toolchains"
  it should "be loaded from YAML files" in {
    ProjectConfig.getConfig().foreach(c => {
      KernelLogger.info(s"got config: ${JsonHelper(c)}")
    })
  }
}
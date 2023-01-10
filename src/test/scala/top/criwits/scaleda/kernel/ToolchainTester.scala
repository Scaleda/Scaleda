package top.criwits.scaleda
package kernel

import kernel.project.config.ProjectConfig
import kernel.utils.{JsonHelper, KernelLogger}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class ToolchainTester extends AnyFlatSpec with should.Matchers {
  val testPath = "../scaleda-sample-project"

  behavior of "Toolchains"
  it should "be loaded from YAML files" in {
    require(ProjectConfig.getConfig().exists(c => {
      KernelLogger.info(s"got config: ${JsonHelper(c)}")
      true
    }))
  }

}
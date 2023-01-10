package top.criwits.scaleda
package kernel.toolchain


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

  it should "run async / await" in {
    import scala.async.Async.{async, await}
    import scala.concurrent.ExecutionContext.Implicits.global
    val future = async {
      val f1 = async {
        Thread.sleep(500)
        44
      }
      val f2 = async {
        Thread.sleep(300)
        42
      }
      await(f1) + await(f2)
    }
    val r: Int = Await.result(future, 10.seconds)
    KernelLogger.info(s"result = $r")
  }
}
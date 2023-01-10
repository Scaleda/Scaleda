package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import top.criwits.scaleda.kernel.utils.KernelLogger

import scala.concurrent.Await

class GeneralTester extends AnyFlatSpec with should.Matchers {
  behavior of "General"

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

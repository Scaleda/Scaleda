package top.scaleda.kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class KernelTests extends AnyFlatSpec with should.Matchers {
  it should "read .env file" in {
    val env = top.scaleda.kernel.utils.EnvironmentUtils.Backup.env
    env should not be empty
    // for ((k, v) <- env) {
    //   println(s"$k=$v")
    // }
  }
}

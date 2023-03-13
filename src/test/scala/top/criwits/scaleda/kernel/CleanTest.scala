package top.criwits.scaleda
package kernel

import kernel.bin.ExtractAssets
import kernel.utils.ScaledaClean

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CleanTest extends AnyFlatSpec with should.Matchers {
  behavior of "ScaledaClean"
  it should "install asserts and clean" in {
    ExtractAssets.run()
    ScaledaClean.run()
  }
}

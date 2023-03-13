package top.criwits.scaleda
package kernel

import kernel.utils.KernelLogger

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class RvcdTest extends AnyFlatSpec with should.Matchers {
  behavior of "Rvcd"
  it should "call rvcd to open file" in {
    // ERROR: cannot find `RvcdOpenFile`
    // val (client, shutdown) = top.criwits.scaleda.kernel.rvcd.Rvcd()
    // client.openFile(RvcdOpenFile.of("/home/chiro/programs/rvcd/data/testbench0.vcd"))
    // KernelLogger.info("done")
    // shutdown()
  }
}

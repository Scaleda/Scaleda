package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.utils.KernelFileUtils

import java.io.{File, PrintWriter}

class FileUtilsTester extends AnyFlatSpec with should.Matchers {
  val testVerilog =
    """module li_tangDingZhen114514 #(
      |  parameter yiwu = 15,
      |  parameter xuebao = 1919810
      |) (
      |  input clk,
      |  input rst,
      |  output dingzhen
      |);
      |
      |  always @ (posedge clk or posedge rst) begin
      |    if (rst) dingzhen = 0;
      |    else dingzhen = 1;
      |  end
      |
      |endmodule
      |""".stripMargin

  val testFileName = "testLoad.v"
  val testFile = new File(testFileName)

  behavior of "KernelFileUtils"

  it should "generate test verilogs" in {
    val printWriter = new PrintWriter(testFile)
    printWriter.write(testVerilog)
    printWriter.close()
  }

  it should "parse verilog module name" in {
    assert(KernelFileUtils.getModuleTitle(testFile).head == "li_tangDingZhen114514")
  }

  it should "delete test file" in {
    testFile.delete()
  }
}

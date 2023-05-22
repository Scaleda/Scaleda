package top.scaleda
package kernel

import kernel.utils.KernelFileUtils
import verilog.parser.{VerilogLexer, VerilogParser, VerilogParserBaseVisitor}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.{File, FileInputStream, PrintWriter}
import scala.collection.mutable
import scala.io.Source

class DumpvcdInsertTester extends AnyFlatSpec with should.Matchers {
  val testVerilog =
    """module a();
      |endmodule
      |
      |module li_tangDingZhen114514 #(
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
      |
      |""".stripMargin

  val testFileName = "testLoad.v"
  val testFile     = new File(testFileName)
  val outFile = new File("testOutput.v")

  behavior of "DumpvcdInsert"

  it should "generate test verilogs" in {
    val printWriter = new PrintWriter(testFile)
    printWriter.write(testVerilog)
    printWriter.close()
  }

  it should "Insert $dumpvcd" in {
    KernelFileUtils.insertAfterModuleHead(
      testFile,
      outFile,
      "li_tangDingZhen114514",
      """
        |initial begin
        |  $dumpvcd("dingzhen.vcd");
        |  $dumpvar
        |end
        |""".stripMargin
    )

    val source = Source.fromFile(outFile)
    println(source.mkString)
    source.close()

  }

  it should "delete test file" in {
    testFile.delete()
    outFile.delete()
  }
}

package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.io.Source

class VerilogParseTest extends AnyFlatSpec with should.Matchers {
  behavior of "VerilogParse"
  it should "test parse module name and instance" in {
    val code   = Source.fromResource("verilog/test.v").mkString
    val result = verilog.utils.ModuleUtils.parseVerilogCodeModules(code)
    println(result)
  }
}

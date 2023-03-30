package top.criwits.scaleda
package kernel

import verilog.utils.ModuleUtils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.File
import scala.io.Source

class VerilogParseTest extends AnyFlatSpec with should.Matchers {
  behavior of "VerilogParse"
  it should "test parse module name and instance" in {
    val code   = Source.fromResource("verilog/test.v").mkString
    val result = verilog.utils.ModuleUtils.parseVerilogCodeModules(code)
    println(result)
  }
  it should "test parse top module" in {
    val path = new File("/home/chiro/Vivado/Tests/ip_tests/ip_tests.srcs/sources_1/new")
    // val path = new File("/home/chiro/Vivado/Tests/ip_tests/ip_tests.srcs/")
    if (path.exists()) {
      val tops = ModuleUtils.parseSourceSetTopModules(Set(path.getAbsolutePath))
      println(s"tops: ${tops}")
    }
  }
}

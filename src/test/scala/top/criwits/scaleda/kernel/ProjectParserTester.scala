package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.project.detect.VivadoProjectConfig
import top.criwits.scaleda.kernel.utils.serialise.XMLHelper

import scala.io.Source

class ProjectParserTester extends AnyFlatSpec with should.Matchers {
  behavior of "ProjectParser"

  it should "test Vivado project parser" in {
    val fileContent = Source
      .fromInputStream(getClass.getClassLoader.getResourceAsStream("vivado/project/ip_tests.xpr"))
      .getLines()
      .mkString
    val obj = XMLHelper(fileContent, classOf[VivadoProjectConfig])
    println(s"obj: ${obj}")
  }
}

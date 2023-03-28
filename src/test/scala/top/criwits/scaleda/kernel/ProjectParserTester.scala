package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.project.detect.VivadoProjectConfig
import top.criwits.scaleda.kernel.project.importer.VivadoTargetParser
import top.criwits.scaleda.kernel.utils.serialise.XMLHelper

import java.io.File
import scala.io.Source

class ProjectParserTester extends AnyFlatSpec with should.Matchers {
  behavior of "ProjectParser"

  it should "test Vivado project config parser" in {
    val fileContent = Source
      .fromInputStream(getClass.getClassLoader.getResourceAsStream("vivado/project/ip_tests.xml"))
      .getLines()
      .mkString
    val obj = XMLHelper(fileContent, classOf[VivadoProjectConfig])
    println(s"obj: ${obj}")
  }

  it should "test vivado parse config and directory" in {
    val path = "/home/chiro/Vivado/Tests/ip_tests/"
    val file = new File(path)
    if (file.exists()) {
      val parser = new VivadoTargetParser
      val target = parser.parseAsTarget(file)
      println(target)
    }
  }
}

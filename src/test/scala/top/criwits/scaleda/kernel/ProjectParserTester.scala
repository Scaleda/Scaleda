package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.io.Source

class ProjectParserTester extends AnyFlatSpec with should.Matchers {
  behavior of "ProjectParser"

  it should "test Vivado project parser" in {
    val fileContent = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("vivado/project")).getLines().mkString
    println(s"fileContent = ${fileContent}")
  }
}

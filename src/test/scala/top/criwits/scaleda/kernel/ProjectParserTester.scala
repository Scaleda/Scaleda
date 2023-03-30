package top.criwits.scaleda
package kernel

import kernel.project.detect.VivadoProjectConfig
import kernel.project.importer.VivadoTargetParser
import kernel.utils.serialise.XMLHelper

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

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
    println(obj.fileSets.map(_.files.map(_.Path)))
    // FIXME: Vivado target parser cannot get file path, only directory done
    require(obj.fileSets.forall(_.files.forall(_.Path.nonEmpty)), "File Path parse failed!")
    println(s"obj: ${obj}")
  }

  it should "test vivado parse config and directory" in {
    val path = "/home/chiro/Vivado/Tests/ip_tests/"
    val file = new File(path)
    if (file.exists()) {
      val parser = new VivadoTargetParser {}
      val target = parser.parseAsTarget(file)
      println(target)
      println(target.tasks.toList)
    }
  }
}
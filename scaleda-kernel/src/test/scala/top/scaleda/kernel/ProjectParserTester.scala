package top.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.scaleda.kernel.project.config.ProjectConfig
import top.scaleda.kernel.project.detect.VivadoProjectConfig
import top.scaleda.kernel.project.importer.VivadoTargetParser
import top.scaleda.kernel.utils.serialise.{XMLHelper, YAMLHelper}

import java.io.File
import java.util.regex.Pattern
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

  it should "ignore unknown fields" in {
    val fileContent = Source
      .fromInputStream(getClass.getClassLoader.getResourceAsStream("vivado/project/adder.xml"))
      .getLines()
      .mkString
    val obj = XMLHelper(fileContent, classOf[VivadoProjectConfig])
    println(obj)
  }

  it should "test vivado parse config and directory" in {
    val path = "/home/chiro/Vivado/Tests/ip_tests/"
    val file = new File(path)
    if (file.exists()) {
      val parser  = new VivadoTargetParser {}
      val project = parser.parseAsProject(file)
      println(project)
      println(project.targets.toList)
    }
  }

  it should "load and save project" in {
    val path = new File("/home/chiro/Vivado/Tests/ip_tests/scaleda.yml")
    if (path.exists()) {
      val project = YAMLHelper(path, classOf[ProjectConfig])
      ProjectConfig.saveConfig(project, targetFile = new File("target", "test.yml"))
    }
  }

  it should "modify source sets" in {
    val fileContent = Source
      .fromInputStream(getClass.getClassLoader.getResourceAsStream("vivado/project/ip_tests.xml"))
      .getLines()
      .mkString
    val pattern =
      """<FileSet\s+Name="(?<name>[^"]*)"\s+Type="DesignSrcs"\s+RelSrcDir="(?<dir>[^"]+)"[^>]*>.*</FileSet>""".r
    val matches = pattern.findAllMatchIn(fileContent)
    // print them
    for (m <- matches) {
      println(m.subgroups)
      println(m.matched)
    }
    val pattern2 = Pattern.compile(
      """<FileSet\s+Name="(?<name>[^"]*)"\s+Type="DesignSrcs"\s+RelSrcDir="(?<dir>[^"]+)"[^>]*>((?!</FileSet>).)*</FileSet>"""
    )
    val m2 = pattern2.matcher(fileContent)
    while (m2.find()) {
      println("================================")
      println(m2.group())
    }
  }
}

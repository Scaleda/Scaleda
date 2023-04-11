package top.criwits.scaleda
package kernel

import kernel.project.config.ProjectConfig
import kernel.project.{ManifestManager, ProjectManifest}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.{File, PrintWriter}

class ConfigLoaderTester extends AnyFlatSpec with should.Matchers {
  val demoConfig =
    """name: test-project
      |type: RTL
      |source: src/
      |topModule: rootTopModule
      |targets:
      |  - name: Icarus 1
      |    toolchain: iverilog
      |    topModule: icarusRootModule
      |    tasks:
      |      - name: Icarus 1 sim
      |        type: simulation
      |        topModule: icarusOneModule
      |      - name: Icarus 1 sim 2
      |        type: simulation
      |  - name: Icarus 2
      |    toolchain: iverilog
      |    tasks:
      |      - name: Icarus 2 sim
      |      - type: simulation
      |
      |""".stripMargin

  val testFileName = "test-config.yml"
  val testFile     = new File(testFileName)

  behavior of "ConfigLoader"

  it should "generate test yaml" in {
    val printWriter = new PrintWriter(testFile)
    printWriter.write(demoConfig)
    printWriter.close()
  }

  it should "load config and parse successfully" in {
    implicit val manifest: ProjectManifest = ManifestManager.getManifest()
    manifest.configFile = Some(testFile.getAbsolutePath)
    manifest.projectBase = Some(testFile.getParent)

    val config = ProjectConfig.getConfig.get
    assert(config.targets(0).tasks(0).findTopModule.get == "icarusOneModule")
    assert(config.targets(0).tasks(1).findTopModule.get == "icarusRootModule")
    assert(config.targets(1).tasks(0).findTopModule.get == "rootTopModule")
  }

  it should "clear test yaml" in {
    testFile.delete()
  }

}

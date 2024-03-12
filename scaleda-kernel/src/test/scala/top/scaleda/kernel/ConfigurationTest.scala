package top.scaleda
package kernel

import kernel.configuration.ScaledaKernelConfiguration
import kernel.project.ManifestManager
import kernel.utils.serialise.XMLHelper

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ConfigurationTest extends AnyFlatSpec with should.Matchers {
  behavior of "Configuration"
  it should "test configuration parser" in {
    val testDataSimple =
      """<component name="test">
        |</component>""".stripMargin
    val testData =
      """<component name="ProjectRunConfigurationManager">
        |  <configuration default="false" name="Unnamed" type="SCALEDA_TASKS_RUN_CONFIGURATION" factoryName="Scaleda">
        |    <scaleda taskName="Run iverilog simulation" targetName="Icarus" profileName="Icarus Verilog" profileHost="" />
        |    <method v="2" />
        |  </configuration>
        |</component>""".stripMargin
    val objSimple = XMLHelper(testDataSimple, classOf[ScaledaKernelConfiguration])
    require(objSimple.name == "test")
    println(s"objSimple = $objSimple")
    val obj = XMLHelper(testData, classOf[ScaledaKernelConfiguration])
    println(s"obj = $obj")

    val create     = ScaledaKernelConfiguration("testConf", "target", "task", "profile", "remote")
    val createData = XMLHelper(create)
    println(createData)

    val manifest = ManifestManager.getManifest()
    manifest.projectBase = Some("/home/chiro/programs/scaleda-sample-project/")
    println(ScaledaKernelConfiguration.configurations(manifest = manifest))
  }
}

package top.criwits.scaleda
package kernel.configuration

import kernel.utils.serialise.XMLHelper

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "component")
class ScaledaKernelConfiguration {
  @JacksonXmlProperty(isAttribute = true)
  var name: String = "ProjectRunConfigurationManager"

  class Configuration {
    @JacksonXmlProperty
    var `default`: String = "false"
    @JacksonXmlProperty
    var name: String = ""
    @JacksonXmlProperty
    var `type`: String = "SCALEDA_TASKS_RUN_CONFIGURATION"
    @JacksonXmlProperty
    var factoryName: String = "Scaleda"

    class Scaleda {
      @JacksonXmlProperty
      var taskName: String = ""
      @JacksonXmlProperty
      var targetName: String = ""
      @JacksonXmlProperty
      var profileName: String = ""
      @JacksonXmlProperty
      var profileHost: String = ""
    }
    @JacksonXmlProperty
    var scaleda = new Scaleda

    class Method {
      var v: String = "2"
    }
    @JacksonXmlProperty
    var method = new Method
  }

  @JacksonXmlProperty
  var configuration = new Configuration
}

object ScaledaKernelConfiguration {
  def apply(
      targetName: String,
      taskName: String,
      profileName: String,
      profileHost: String
  ): ScaledaKernelConfiguration = {
    val c = new ScaledaKernelConfiguration
    c.configuration.scaleda.targetName = targetName
    c.configuration.scaleda.taskName = taskName
    c.configuration.scaleda.profileName = profileName
    c.configuration.scaleda.profileHost = profileHost
    c
  }
}

object ScaledaKernelConfigurationTest extends App {
  private val testDataSimple = """<component name="test">
                   |</component>""".stripMargin
  private val testData       = """<component name="ProjectRunConfigurationManager">
                   |  <configuration default="false" name="Unnamed" type="SCALEDA_TASKS_RUN_CONFIGURATION" factoryName="Scaleda">
                   |    <scaleda taskName="Run iverilog simulation" targetName="Icarus" profileName="Icarus Verilog" profileHost="" />
                   |    <method v="2" />
                   |  </configuration>
                   |</component>""".stripMargin
  val objSimple              = XMLHelper(testDataSimple, classOf[ScaledaKernelConfiguration])
  require(objSimple.name == "test")
  println(s"objSimple = $objSimple")
  val obj = XMLHelper(testData, classOf[ScaledaKernelConfiguration])
  println(s"obj = $obj")

  val create     = ScaledaKernelConfiguration("target", "task", "profile", "remote")
  val createData = XMLHelper(create)
  println(createData)
}

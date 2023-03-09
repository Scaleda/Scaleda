package top.criwits.scaleda
package kernel.configuration

import kernel.project.config.ProjectConfig
import kernel.utils.Paths
import kernel.utils.serialise.XMLHelper

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

import java.io.{File, FilenameFilter}

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

  override def toString =
    s"Configuration(name=${configuration.name}, target=${configuration.scaleda.targetName}, " +
      s"profile=${configuration.scaleda.profileName}, profileHost=${configuration.scaleda.profileHost})"
}

object ScaledaKernelConfiguration {
  def apply(
      name: String,
      targetName: String,
      taskName: String,
      profileName: String,
      profileHost: String
  ): ScaledaKernelConfiguration = {
    val c = new ScaledaKernelConfiguration
    c.configuration.name = name
    c.configuration.scaleda.targetName = targetName
    c.configuration.scaleda.taskName = taskName
    c.configuration.scaleda.profileName = profileName
    c.configuration.scaleda.profileHost = profileHost
    c
  }

  /** Get all configurations in .run dir
    * @return Map filename(no ext) -> configuration
    */
  def configurations: Map[String, ScaledaKernelConfiguration] = {
    val projectDirString = ProjectConfig.projectBase.getOrElse(Paths.pwd.getAbsolutePath)
    val dotRunDir        = new File(projectDirString, ".run")
    if (!dotRunDir.exists() || (dotRunDir.exists() && !dotRunDir.isDirectory)) return Map()
    val files = dotRunDir.listFiles(new FilenameFilter {
      override def accept(file: File, s: String) = s.endsWith(".run.xml")
    })
    files
      .map(f => {
        f.getName.split("\\.run\\.xml").head -> {
          XMLHelper(f, classOf[ScaledaKernelConfiguration])
        }
      })
      .filter(_._2.configuration.name.nonEmpty)
      .toMap
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

  val create     = ScaledaKernelConfiguration("testConf", "target", "task", "profile", "remote")
  val createData = XMLHelper(create)
  println(createData)

  ProjectConfig.projectBase = Some("/home/chiro/programs/scaleda-sample-project/")
  println(ScaledaKernelConfiguration.configurations)
}

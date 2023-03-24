package top.criwits.scaleda
package kernel.project.detect

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonInclude}
import com.fasterxml.jackson.dataformat.xml.annotation.{
  JacksonXmlElementWrapper,
  JacksonXmlProperty,
  JacksonXmlRootElement
}

class OptionData {
  var Name = ""
  var Val  = ""
}

class ConfigurationData {
  @JacksonXmlElementWrapper(localName = "Option", useWrapping = true)
  var options: Seq[OptionData] = Seq()
}

@JsonIgnoreProperties(
  Array(
    "DefaultLaunch",
    "FileSets", // TODO
    "Simulators",
    "Runs", // TODO
    "Board",
    "DashboardSummary" // TODO
  )
)
@JsonInclude(Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "Project")
class VivadoProjectConfig {
  // Support Version=7, Minor do not consider now
  @JacksonXmlProperty(isAttribute = true)
  var Version: String = "7"
  @JacksonXmlProperty(isAttribute = true)
  // Vivado 2019.2
  var Minor: String = "44"
  // Absolute path of this project
  @JacksonXmlProperty(isAttribute = true)
  var Path: String = ""

  @JacksonXmlProperty(localName = "Configuration")
  var configuration: ConfigurationData = new ConfigurationData
}

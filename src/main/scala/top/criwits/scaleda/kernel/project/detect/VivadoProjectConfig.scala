package top.criwits.scaleda
package kernel.project.detect

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "Project")
class VivadoProjectConfig {
  // Support Version=7, Minor do not consider now
  @JacksonXmlProperty(isAttribute = true)
  var Version: String = "7"
  @JacksonXmlProperty(isAttribute = true)
  // Vivado 2019.2
  var Minor: String = "44"
  @JacksonXmlProperty(localName = "Option")
  class ConfigurationData {
    class OptionData {
      var Name = ""
      var Val = ""
    }
    var options: Seq[OptionData] = Seq()
  }
}

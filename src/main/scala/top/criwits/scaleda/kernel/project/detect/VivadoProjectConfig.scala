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

class AttrData {
  var Name = ""
  var Val  = ""
}

@JsonIgnoreProperties(Array("FileInfo", "Attr"))
class FileData {
  @JacksonXmlProperty(isAttribute = true)
  var Path = ""
  // @JacksonXmlElementWrapper(localName = "FileInfo", useWrapping = false)
  // var fileInfo: Seq[AttrData] = Seq()
}
class ConfigurationData {
  @JacksonXmlElementWrapper(localName = "Option")
  var options: Seq[OptionData] = Seq()
}

@JsonIgnoreProperties(Array("Filter"))
class FileSet {
  @JacksonXmlProperty(isAttribute = true)
  var Name = "sources_1"

  /** DesignSrcs / Constrs / SimulationSrcs / Utils / BlockSrcs
    */
  @JacksonXmlProperty(isAttribute = true)
  var Type = "DesignSrcs"
  @JacksonXmlProperty(isAttribute = true)
  var RelSrcDir = "$PSRCDIR/sources_1"

  @JacksonXmlElementWrapper(localName = "Config")
  var config: Seq[OptionData] = Seq()

  @JacksonXmlElementWrapper(localName = "File")
  var files: Seq[FileData] = Seq()
}

@JsonIgnoreProperties(
  Array(
    "DefaultLaunch",
    "Simulators",
    "Runs", // TODO
    "Board",
    "DashboardSummary" // TODO
  )
)
@JsonInclude(Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "Project")
class VivadoProjectConfig {
  // Support Version == 7, Minor do not consider now
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

  @JacksonXmlProperty(localName = "FileSets")
  var fileSets: Seq[FileSet] = Seq()
}

package top.criwits.scaleda
package toolchain

import com.intellij.openapi.externalSystem.settings.ExternalProjectSettings
import com.intellij.util.xmlb.annotations.OptionTag

import java.io.File
import java.util.UUID
import scala.beans.BeanProperty

class ToolchainProfile {
  @BeanProperty
  @OptionTag(converter = classOf[UUIDConverter])
  var uuid: UUID = UUID.randomUUID();

  @BeanProperty
  var name: String = "New Profile"

  @BeanProperty
  @OptionTag(converter = classOf[ToolchainConverter])
  var kind: ToolchainType = _

  @BeanProperty
  @OptionTag(converter = classOf[FileConverter]) var path: File = _
}



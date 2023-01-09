package top.criwits.scaleda
package kernel.toolchain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.io.File

/**
 * Class for a profile for a specific toolchain
 */
class ToolchainProfile(var profileName: String,
                       var toolchainType: String,
                       var path: String) {
  /**
   * Handler to it's yaml file
   */
  @JsonIgnore
  var file: Option[File] = None
  @JsonIgnore
  var edited: Boolean = false
  @JsonIgnore
  var removed: Boolean = false

  def verify(): (Int, Option[String]) = {
    toolchainType match {
      case Vivado.internalID => Vivado.verify(this)
      case _ => (-2, None)
    }
  }
}

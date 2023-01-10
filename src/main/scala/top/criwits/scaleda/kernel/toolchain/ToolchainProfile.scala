package top.criwits.scaleda
package kernel.toolchain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.kernel.shell.command.CommandDeps
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.io.File
import scala.concurrent.Future

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

  /**
   * Verify current toolchain status
   * @return
   */
  @JsonIgnore
  def getVerifier: Option[ToolchainProfile.Verifier] = {
    toolchainType match {
      case Vivado.internalID => Some(new Vivado.Verifier(this))
      case _ => None
    }
  }
}

object ToolchainProfile {
  /**
   * Verifier for profile
   */
  abstract class Verifier(val toolchainProfile: ToolchainProfile) {
    def verifyCommandLine: Option[CommandDeps]
    def parseVersionInfo(returnValue: Int, stdOut: String): (Boolean, Option[String])
  }
}

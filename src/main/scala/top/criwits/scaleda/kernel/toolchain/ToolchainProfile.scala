package top.criwits.scaleda
package kernel.toolchain

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.kernel.shell.command.CommandDeps
import top.criwits.scaleda.kernel.toolchain.impl.Vivado

import java.io.File
import scala.concurrent.Future

/**
 * Class for a profile for a specific toolchain
 */
@JsonInclude(Include.NON_NULL)
class ToolchainProfile(var profileName: String,
                       var toolchainType: String,
                       var path: String, /* For Vivado / PDS / Quartus */
                       var iverilogPath: String, /* For iverilog */
                       var vvpPath: String,
                       var iverilogVPIPath: String) {
  /**
   * Handler to it's yaml file
   */
  @JsonIgnore
  var file: Option[File] = None
  @JsonIgnore
  var edited: Boolean = false
  @JsonIgnore
  var removed: Boolean = false

  def this(profileName: String, toolchainType: String) = {
    this(profileName, toolchainType, "", "", "", "")
  }

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
   *
   * A [[Verifier]] is a class to verify whether a [[ToolchainProfile]] meets the requirement of that kind of toolchain.
   */
  abstract class Verifier(val toolchainProfile: ToolchainProfile) {
    /**
     * Generate command line(s) used to verify toolchain profile
     * @return One or more than one command line(s)
     */
    def verifyCommandLine: Option[Seq[CommandDeps]]

    /**
     * Parse toolchain version information from output of command lines of [[verifyCommandLine]]
     * @param returnValues Return values of commands
     * @param outputs Output strings of commands
     * @return
     */
    def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String])
  }
}

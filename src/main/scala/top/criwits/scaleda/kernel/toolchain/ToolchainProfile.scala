package top.criwits.scaleda
package kernel.toolchain

import kernel.net.remote.RemoteProfile
import kernel.shell.command.CommandDeps
import kernel.toolchain.impl.{IVerilog, Vivado}

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}

import java.io.File

/** Class for a profile for a specific toolchain
  */
@JsonInclude(Include.NON_EMPTY)
class ToolchainProfile(
    var profileName: String,
    var toolchainType: String,
    var path: String, /* For Vivado / PDS / Quartus */
    var iverilogPath: String, /* For iverilog */
    var vvpPath: String,
    var iverilogVPIPath: String
) {

  /** Handler to it's yaml file
    */
  @JsonIgnore
  var file: Option[File] = None
  @JsonIgnore
  var edited: Boolean = false
  @JsonIgnore
  var removed: Boolean = false
  @JsonIgnore
  var host: String = ""

  def this(profileName: String, toolchainType: String) = {
    this(profileName, toolchainType, "", "", "", "")
  }

  def this(profileName: String, toolchainType: String, path: String) = {
    this(profileName, toolchainType, path, "", "", "")
  }

  /** Verify current toolchain status
    * @return
    */
  @JsonIgnore
  def getVerifier: Option[ToolchainProfile.Verifier] = {
    toolchainType match {
      case Vivado.internalID   => Some(new Vivado.Verifier(this))
      case IVerilog.internalID => Some(new IVerilog.Verifier(this))
      case _                   => None
    }
  }

  @JsonIgnore
  private def stringRemoveNull(s: String): String = if (s != null) s else ""

  @JsonIgnore
  def asRemoteProfile = RemoteProfile.of(
    stringRemoveNull(profileName),
    stringRemoveNull(toolchainType),
    stringRemoveNull(path),
    stringRemoveNull(iverilogPath),
    stringRemoveNull(vvpPath),
    stringRemoveNull(iverilogVPIPath)
  )
}

object ToolchainProfile {

  /** Verifier for profile
    *
    * A [[Verifier]] is a class to verify whether a [[ToolchainProfile]] meets the requirement of that kind of toolchain.
    */
  abstract class Verifier(val toolchainProfile: ToolchainProfile) {

    /** Generate command line(s) used to verify toolchain profile
      * @return One or more than one command line(s)
      */
    def verifyCommandLine: Option[Seq[CommandDeps]]

    /** Parse toolchain version information from output of command lines of [[verifyCommandLine]]
      * @param returnValues Return values of commands
      * @param outputs Output strings of commands
      * @return
      */
    def parseVersionInfo(returnValues: Seq[Int], outputs: Seq[String]): (Boolean, Option[String])
  }

  def asRemoteToolchainProfile(remote: RemoteProfile, host: String): ToolchainProfile = {
    import remote._
    val v = new ToolchainProfile(profileName, toolchainType, path, iverilogPath, vvpPath, iverilogVPIPath)
    v.host = host
    v
  }
}

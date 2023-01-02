package top.criwits.scaleda
package kernel.toolchain

import org.jetbrains.annotations.Nls

import java.io.File

/**
 * Base class for a profile for a specific toolchain
 */
abstract class ToolchainProfile(val toolchain: String, @Nls val profileName: String) {
  var file: File = _
}

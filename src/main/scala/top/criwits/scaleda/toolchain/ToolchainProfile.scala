package top.criwits.scaleda
package toolchain

import java.io.File
import java.util.UUID

class ToolchainProfile(val uuid: UUID,
                       val name: String,
                       val kind: ToolchainType,
                       val path: File) {
}



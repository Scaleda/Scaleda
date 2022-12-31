package top.criwits.scaleda
package kernel.toolchain

import com.intellij.util.xmlb.Converter
import top.criwits.scaleda.kernel.toolchain.impl.{PDS, Vivado}

class ToolchainConverter extends Converter[ToolchainType] {
  override def fromString(value: String): ToolchainType = value match {
    case "vivado" => new Vivado
    case "pds"    => new PDS
  }

  override def toString(value: ToolchainType): String = value.internalID
}

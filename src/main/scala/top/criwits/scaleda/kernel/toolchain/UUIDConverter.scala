package top.criwits.scaleda
package kernel.toolchain

import com.intellij.util.xmlb.Converter

import java.util.UUID

class UUIDConverter extends Converter[UUID]{
  override def fromString(value: String): UUID = UUID.fromString(value)

  override def toString(value: UUID): String = value.toString
}

package top.criwits.scaleda
package toolchain

import com.intellij.util.xmlb.Converter

import java.io.File

class FileConverter extends Converter[File] {
  override def fromString(value: String): File = new File(value)

  override def toString(value: File): String = value.getAbsolutePath
}

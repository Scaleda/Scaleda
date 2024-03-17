package top.scaleda
package systemverilog

import idea.utils.Icons

import com.intellij.openapi.fileTypes.{FileType, LanguageFileType}

import javax.swing.Icon

final class SystemVerilogFileType extends LanguageFileType(SystemVerilogLanguage) {
  override def getName: String = "SystemVerilog"

  override def getDescription: String = "SystemVerilog"

  override def getDefaultExtension: String = SystemVerilogFileType.DefaultExtension

  override def getIcon: Icon = SystemVerilogFileType.DefaultIcon
}

object SystemVerilogFileType {
  private final val DefaultExtension = "systemVerilog"
  final val DefaultIcon = Icons.systemVerilog
  val instance = new SystemVerilogFileType

  def isSystemVerilog(fileType: FileType): Boolean = fileType match {
    case _: SystemVerilogFileType => true
    case _ => false
  }
}
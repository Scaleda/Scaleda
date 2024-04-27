package top.scaleda
package verilog

import idea.utils.Icons
import kernel.language.LangVerilog
import verilog.VerilogLogger.Log

import com.intellij.openapi.fileTypes.{FileType, LanguageFileType}

import javax.swing.Icon

final class VerilogFileType extends LanguageFileType(VerilogLanguage) {
  override def getName: String = LangVerilog.getName

  override def getDescription: String = "Verilog"

  override def getDefaultExtension: String = VerilogFileType.DefaultExtension

  override def getIcon: Icon = VerilogFileType.DefaultIcon
}

object VerilogFileType {
  private final val DefaultExtension = "v"
  final val DefaultIcon = Icons.verilog
  val instance = new VerilogFileType

  Log.debug(s"DefaultIcon size = ${DefaultIcon.getIconWidth}x${DefaultIcon.getIconHeight}")

  def isVerilog(fileType: FileType): Boolean = fileType match {
    case _: VerilogFileType => true
    case _ => false
  }
}
package top.scaleda
package systemverilog.editor.formatter

import systemverilog.{SystemVerilogFileType, SystemVerilogLanguage, SystemVerilogPSIFileRoot}
import verilog.editor.formatter.VerilogExternalFormatter

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.PsiFile

class SystemVerilogExternalFormatter extends VerilogExternalFormatter {
  override def getName: String = "SystemVerilog"

  override def canFormat(file: PsiFile): Boolean = file.isInstanceOf[SystemVerilogPSIFileRoot]

  override def getFileType: LanguageFileType = SystemVerilogFileType.instance

  override def getLanguage: Language = SystemVerilogLanguage
}

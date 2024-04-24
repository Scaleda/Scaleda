package top.scaleda
package bluespec.editor.formatter

import bluespec.{BluespecFileType, BluespecLanguage, BluespecPSIFileRoot}
import verilog.editor.formatter.VerilogExternalFormatter

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.PsiFile
import top.scaleda.bluespec

class BluespecExternalFormatter extends VerilogExternalFormatter {
  override def getName: String = "Bluespec"

  override def canFormat(file: PsiFile): Boolean = file.isInstanceOf[bluespec.BluespecPSIFileRoot]

  override def getFileType: LanguageFileType = bluespec.BluespecFileType.instance

  override def getLanguage: Language = BluespecLanguage
}

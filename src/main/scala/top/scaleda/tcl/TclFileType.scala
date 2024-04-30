package top.scaleda
package tcl

import idea.utils.Icons
import kernel.language.LangTcl

import com.intellij.openapi.fileTypes.{FileType, LanguageFileType}

import javax.swing.Icon

final class TclFileType extends LanguageFileType(TclLanguage.INSTANCE) {
  override def getName: String = LangTcl.getName

  override def getDescription: String = LangTcl.getDescription

  override def getDefaultExtension: String = TclFileType.DefaultExtension

  override def getIcon: Icon = TclFileType.DefaultIcon
}

object TclFileType {
  private final val DefaultExtension = LangTcl.getDefaultExtension
  final val DefaultIcon = Icons.tcl
  val instance = new TclFileType

  def isTcl(fileType: FileType): Boolean = fileType match {
    case _: TclFileType => true
    case _ => false
  }
}
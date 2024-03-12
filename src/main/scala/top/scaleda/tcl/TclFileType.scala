package top.scaleda
package tcl

import com.intellij.openapi.fileTypes.{FileType, LanguageFileType}
import top.scaleda.idea.utils.Icons

import javax.swing.Icon

final class TclFileType extends LanguageFileType(TclLanguage) {
  override def getName: String = "Tcl"

  override def getDescription: String = "Tcl"

  override def getDefaultExtension: String = TclFileType.DefaultExtension

  override def getIcon: Icon = TclFileType.DefaultIcon
}

object TclFileType {
  private final val DefaultExtension = "tcl"
  final val DefaultIcon = Icons.tcl
  val instance = new TclFileType

  def isTcl(fileType: FileType): Boolean = fileType match {
    case _: TclFileType => true
    case _ => false
  }
}
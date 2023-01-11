package top.criwits.scaleda
package tcl

import idea.utils.Icons
import tcl.TclLogger.Log

import com.intellij.openapi.fileTypes.{FileType, LanguageFileType}

import javax.swing.Icon

final class TclFileType extends LanguageFileType(TclLanguage) {
  override def getName: String = "Tcl"

  override def getDescription: String = "Tcl"

  override def getDefaultExtension: String = TclFileType.DefaultExtension

  override def getIcon: Icon = TclFileType.DefaultIcon
}

object TclFileType {
  private final val DefaultExtension = "v"
  final val DefaultIcon = Icons.verilog
  val instance = new TclFileType

  Log.warn(s"DefaultIcon size = ${DefaultIcon.getIconWidth}x${DefaultIcon.getIconHeight}")

  def isTcl(fileType: FileType): Boolean = fileType match {
    case _: TclFileType => true
    case _ => false
  }
}
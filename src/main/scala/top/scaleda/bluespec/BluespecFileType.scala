package top.scaleda
package bluespec

import idea.utils.Icons
import kernel.language.LangBluespec

import com.intellij.openapi.fileTypes.{FileType, LanguageFileType}

import javax.swing.Icon

final class BluespecFileType extends LanguageFileType(BluespecLanguage.INSTANCE) {
  override def getName: String = LangBluespec.getName

  override def getDescription: String = LangBluespec.getDescription

  override def getDefaultExtension: String = BluespecFileType.DefaultExtension

  override def getIcon: Icon = BluespecFileType.DefaultIcon
}

object BluespecFileType {
  private final val DefaultExtension = LangBluespec.getDefaultExtension
  final val DefaultIcon = Icons.bluespec
  val instance = new BluespecFileType

  def isBluespec(fileType: FileType): Boolean = fileType match {
    case _: BluespecFileType => true
    case _ => false
  }
}
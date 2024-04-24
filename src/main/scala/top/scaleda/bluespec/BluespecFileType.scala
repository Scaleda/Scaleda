package top.scaleda
package bluespec

import idea.utils.Icons

import com.intellij.openapi.fileTypes.{FileType, LanguageFileType}

import javax.swing.Icon

final class BluespecFileType extends LanguageFileType(BluespecLanguage) {
  override def getName: String = "Bluespec"

  override def getDescription: String = "Bluespec"

  override def getDefaultExtension: String = BluespecFileType.DefaultExtension

  override def getIcon: Icon = BluespecFileType.DefaultIcon
}

object BluespecFileType {
  private final val DefaultExtension = "bsv"
  final val DefaultIcon = Icons.bluespec
  val instance = new BluespecFileType

  def isBluespec(fileType: FileType): Boolean = fileType match {
    case _: BluespecFileType => true
    case _ => false
  }
}
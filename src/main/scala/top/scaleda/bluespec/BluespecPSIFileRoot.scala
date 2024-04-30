package top.scaleda
package bluespec

import bluespec.psi.nodes

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

import javax.swing.Icon

class BluespecPSIFileRoot(viewProvider: FileViewProvider)
    extends PsiFileBase(viewProvider, BluespecLanguage.INSTANCE)
    with nodes.ScopeNode {

  override def getFileType: FileType =
    BluespecFileType.instance.asInstanceOf[FileType]

  override def toString: String = "Bluespec file"

  override def getIcon(flags: Int): Icon = BluespecFileType.DefaultIcon

  override def getContext: nodes.ScopeNode = null

  // def getAvailableNamedElementsScala: Array[PsiNamedElement] =
  //   PsiTreeUtil
  //     .findChildrenOfType(this, classOf[ModuleDeclarationPsiNode])
  //     .toArray
  //     .map(x => x.asInstanceOf[PsiNamedElement])
  //
  // override def getAvailableNamedElements =
  //   PsiTreeUtil.findChildrenOfType(this, classOf[ModuleDeclarationPsiNode])

  override def getAvailableNamedElements = java.util.List.of()
}

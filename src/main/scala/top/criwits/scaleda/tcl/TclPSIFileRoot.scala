package top.criwits.scaleda
package tcl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import top.criwits.scaleda.verilog.psi.nodes.ScopeNode

import javax.swing.Icon

class TclPSIFileRoot(viewProvider: FileViewProvider)
    extends PsiFileBase(viewProvider, TclLanguage)
    with ScopeNode {

  override def getFileType: FileType =
    TclFileType.instance.asInstanceOf[FileType]

  override def toString: String = "Tcl file"

  override def getIcon(flags: Int): Icon = TclFileType.DefaultIcon

  override def getContext: ScopeNode = null

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

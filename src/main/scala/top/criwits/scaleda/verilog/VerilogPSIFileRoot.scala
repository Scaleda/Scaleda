package top.criwits.scaleda
package verilog

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{FileViewProvider, PsiNamedElement}
import top.criwits.scaleda.verilog.psi.nodes.ScopeNode
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode

import javax.swing.Icon

class VerilogPSIFileRoot(viewProvider: FileViewProvider)
  extends PsiFileBase(viewProvider, VerilogLanguage) /* with ScopeNode */ {

  override def getFileType: FileType = VerilogFileType.instance.asInstanceOf[FileType]

  override def toString: String = "Verilog file"

  override def getIcon(flags: Int): Icon = VerilogFileType.DefaultIcon

  override def getContext: ScopeNode = null

  def getAvailableNamedElementsScala: Array[PsiNamedElement] =
    PsiTreeUtil.findChildrenOfType(this, classOf[ModuleDeclarationPsiNode])
      .toArray
      .map(x => x.asInstanceOf[PsiNamedElement])

//  override def getAvailableNamedElements =
//    PsiTreeUtil.findChildrenOfType(this, classOf[ModuleDeclarationPsiNode])
}

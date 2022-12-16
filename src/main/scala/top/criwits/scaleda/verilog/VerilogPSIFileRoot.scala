package top.criwits.scaleda
package verilog

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.{FileViewProvider, PsiElement, PsiNamedElement}
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.jetbrains.sample.SampleLanguage
import org.antlr.jetbrains.sample.psi.CallSubtree

class VerilogPSIFileRoot(viewProvider: FileViewProvider)
  extends PsiFileBase(viewProvider, VerilogLanguage) with ScopeNode {
  override def resolve(element: PsiNamedElement): PsiElement = {
    if (element.getParent.isInstanceOf[CallSubtree])
      SymtabUtils.resolve(this, SampleLanguage.INSTANCE, element, "/script/function/ID")
    else SymtabUtils.resolve(this, SampleLanguage.INSTANCE, element, "/script/vardef/ID")
  }

  override def getFileType: FileType = VerilogFileType.instance.asInstanceOf[FileType]

  override def toString: String = "Verilog file"

  override def getIcon(flags: Int) = VerilogFileType.DefaultIcon

  override def getContext: ScopeNode = null
}

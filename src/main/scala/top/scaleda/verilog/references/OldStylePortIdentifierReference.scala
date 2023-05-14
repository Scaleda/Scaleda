package top.scaleda
package verilog.references

import verilog.psi.nodes.module.{ModuleDeclarationPsiNode, PortReferencePsiNode}
import verilog.psi.nodes.signal.PortIdentifierPsiNode

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}

class OldStylePortIdentifierReference(element: PortReferencePsiNode)
    extends PsiReferenceBase[PortReferencePsiNode](element, element.getHoldPsiNodeRelativeTextRange) {
  override def resolve(): PsiElement = {
    val moduleDeclaration = PsiTreeUtil.getParentOfType(element, classOf[ModuleDeclarationPsiNode])
    if (moduleDeclaration == null) return null

    val identifiers  = moduleDeclaration.getChildrenInModuleBody(classOf[PortIdentifierPsiNode])
    val myIdentifier = element.getPortIdentifier.orNull
    if (myIdentifier == null || myIdentifier.getName == null) return null
    identifiers.foreach(identifier => {
      if (identifier.getName == myIdentifier.getName) return identifier
    })
    null
  }


  override def getVariants: Array[AnyRef] = Array.empty
}

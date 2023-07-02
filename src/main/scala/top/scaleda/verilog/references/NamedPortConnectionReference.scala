package top.scaleda
package verilog.references

import verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NamedPortConnectionPsiNode}
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.psi.nodes.signal.PortIdentifierPsiNode

import com.intellij.psi.{PsiElement, PsiReferenceBase}
import com.intellij.psi.util.PsiTreeUtil

class NamedPortConnectionReference(element: NamedPortConnectionPsiNode)
    extends PsiReferenceBase[NamedPortConnectionPsiNode](
      element,
      element.getHoldPsiNodeRelativeTextRange
    ) {
  override def resolve: PsiElement = {
    val moduleInstantiationPsiNode = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleInstantiationPsiNode])
    if (moduleInstantiationPsiNode == null) return null

    val ref = moduleInstantiationPsiNode.getReference.resolve
    if (!ref.isInstanceOf[ModuleDeclarationPsiNode]) return null
    ref
      .asInstanceOf[ModuleDeclarationPsiNode]
      .getModuleHead.map(_
      .getPorts
      .find(_.getName == element.getHoldPsiNode.getName)
      .orNull).orNull
  }
}

package top.scaleda
package systemverilog.references

import systemverilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NamedPortConnectionPsiNode}
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}

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

package top.scaleda
package systemverilog.references

import systemverilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NamedParameterAssignmentPsiNode}
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}

class NamedParameterAssignmentReference(element: NamedParameterAssignmentPsiNode)
  extends PsiReferenceBase[NamedParameterAssignmentPsiNode](element, element.getHoldPsiNodeRelativeTextRange) {
  override def resolve(): PsiElement = {
    val moduleInstantiationPsiNode = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleInstantiationPsiNode])
    if (moduleInstantiationPsiNode == null) return null

    val ref = moduleInstantiationPsiNode.getReference.resolve
    if (!ref.isInstanceOf[ModuleDeclarationPsiNode]) return null
    ref.asInstanceOf[ModuleDeclarationPsiNode]
      .getParameters
      .find(element.getHoldPsiNode != null &&  _.getName == element.getHoldPsiNode.getName) // fix NPE
      .orNull
  }
}

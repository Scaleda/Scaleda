package top.criwits.scaleda
package verilog.references

import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}
import top.criwits.scaleda.verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode

class HierarchicalIdentifierReference(element: HierarchicalIdentifierPsiNode)
    extends PsiReferenceBase[HierarchicalIdentifierPsiNode](
      element,
      element.getHoldPsiNodeRelativeTextRange()
    ) {

//    override def resolve(): PsiElement? {
//        return (PsiTreeUtil
//            .getParentOfType(
//                myElement,
//                ModuleDeclarationPsiNode::class.java
//            ) ?: return null)
////                .availableNamedElements
////                .find { it.name == myElement.name }
//    }

  override def resolve(): PsiElement = {
    PsiTreeUtil.getParentOfType(myElement, classOf[ModuleDeclarationPsiNode]) // FIX: need more!
  }

  override def getVariants: Array[AnyRef] = Array.empty
}

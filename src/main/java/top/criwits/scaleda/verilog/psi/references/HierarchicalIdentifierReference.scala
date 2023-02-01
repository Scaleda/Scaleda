package top.criwits.scaleda.verilog.psi.references

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.factory.nodes.HierarchicalIdentifierPsiNode
import top.criwits.scaleda.verilog.psi.factory.nodes.module.ModuleDeclarationPsiNode


class HierarchicalIdentifierReference(element: HierarchicalIdentifierPsiNode) extends
    PsiReferenceBase[HierarchicalIdentifierPsiNode](
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
    PsiTreeUtil.getParentOfType(myElement, classOf[ModuleDeclarationPsiNode])
  }

  override def getVariants: Array[AnyRef] = Array.empty
}
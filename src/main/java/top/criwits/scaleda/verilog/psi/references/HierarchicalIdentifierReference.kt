package top.criwits.scaleda.verilog.psi.references

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.factory.nodes.HierarchicalIdentifierPsiNode
import top.criwits.scaleda.verilog.psi.factory.nodes.ModuleDeclarationPsiNode


class HierarchicalIdentifierReference(element: HierarchicalIdentifierPsiNode) :
    PsiReferenceBase<HierarchicalIdentifierPsiNode>(
        element,
        element.getHoldPsiNodeRelativeTextRange()
    ) {

    override fun resolve(): PsiElement? {
        return (PsiTreeUtil
            .getParentOfType(
                myElement,
                ModuleDeclarationPsiNode::class.java
            ) ?: return null)
//                .availableNamedElements
//                .find { it.name == myElement.name }
    }


    override fun getVariants(): Array<Any> {
        return emptyArray()
    }

}
package top.criwits.scaleda.verilog.psi.references

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.factory.nodes.*


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
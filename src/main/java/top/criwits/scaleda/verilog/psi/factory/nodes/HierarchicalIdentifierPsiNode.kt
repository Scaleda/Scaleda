package top.criwits.scaleda.verilog.psi.factory.nodes

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.references.HierarchicalIdentifierReference


class HierarchicalIdentifierPsiNode(node: ASTNode) : ANTLRPsiNode(node), ReferenceHolder<SimpleIdentifierPsiLeafNode> {

    override fun getName(): String? {
        return this.getHoldPsiNode()?.name
    }

    override fun getHoldPsiNode(): SimpleIdentifierPsiLeafNode? {
        return PsiTreeUtil
            .findChildOfType(this, SimpleIdentifierPsiLeafNode::class.java)
    }

    override fun getHoldPsiNodeRelativeTextRange(): TextRange? {
        return getHoldPsiNode()
            ?.textRange
            ?.shiftLeft(this.textOffset)
    }

    override fun getReference(): PsiReference {
        return HierarchicalIdentifierReference(this)
    }

}

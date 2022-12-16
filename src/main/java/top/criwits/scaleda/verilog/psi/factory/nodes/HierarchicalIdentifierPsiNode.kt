package top.criwits.scaleda.verilog.psi.factory.nodes

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.references.HierarchicalIdentifierReference
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode


class HierarchicalIdentifierPsiNode(node: ASTNode)
    : ANTLRPsiNode(node), ReferenceHolder<SimpleIdentifierPsiLeafNode> {

    override fun getName(): String? {
        return this.getHoldPsiNode()?.name
    }

    public override fun getHoldPsiNode(): SimpleIdentifierPsiLeafNode? {
        return PsiTreeUtil
                .findChildOfType(this, SimpleIdentifierPsiLeafNode::class.java)
    }

    public override fun getHoldPsiNodeRelativeTextRange(): TextRange? {
        return getHoldPsiNode()
                ?.textRange
                ?.shiftLeft(this.textOffset)
    }

    override fun getReference(): PsiReference? {
        return HierarchicalIdentifierReference(this)
    }

}

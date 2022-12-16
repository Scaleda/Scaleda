package top.criwits.scaleda.verilog.psi.factory.nodes

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.references.ModuleInstantiationReference
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ModuleInstantiationPsiNode(node: ASTNode) :
        ANTLRPsiNode(node), ReferenceHolder<ModuleIdentifierPsiNode> {

    public override fun getHoldPsiNode(): ModuleIdentifierPsiNode? {
        return PsiTreeUtil
                .findChildOfType(this, ModuleIdentifierPsiNode::class.java)
    }

    public override fun getHoldPsiNodeRelativeTextRange(): TextRange? {
        return getHoldPsiNode()
                ?.textRange
                ?.shiftLeft(this.textOffset)
    }

    override fun getReference(): PsiReference {
        return ModuleInstantiationReference(this)
    }
}

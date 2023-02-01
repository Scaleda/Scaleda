package top.criwits.scaleda.verilog.psi.factory.nodes

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.references.HierarchicalIdentifierReference


class HierarchicalIdentifierPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with ReferenceHolder[SimpleIdentifierPsiLeafNode] {

    override def getName(): String = {
        this.getHoldPsiNode().getName
    }

    override def getHoldPsiNode(): SimpleIdentifierPsiLeafNode = {
        PsiTreeUtil.findChildOfType(this, classOf[SimpleIdentifierPsiLeafNode])
    }

    override def getHoldPsiNodeRelativeTextRange(): TextRange = {
        getHoldPsiNode()
            .getTextRange
            .shiftLeft(this.getTextOffset)
    }

    override def getReference(): PsiReference = {
        new HierarchicalIdentifierReference(this)
    }

}

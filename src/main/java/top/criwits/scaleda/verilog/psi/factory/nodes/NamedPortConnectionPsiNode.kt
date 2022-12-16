//package top.criwits.scaleda.verilog.psi.factory.nodes
//
//import com.intellij.lang.ASTNode
//import com.intellij.openapi.util.TextRange
//import com.intellij.psi.PsiReference
//import com.intellij.psi.util.PsiTreeUtil
//import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
//import top.criwits.scaleda.verilog.psi.references.NamedPortConnectionReference
//
//class NamedPortConnectionPsiNode(node: ASTNode) : ANTLRPsiNode(node), ReferenceHolder<PortIdentifierPsiNode> {
//
//    override fun getReference(): PsiReference {
//        return NamedPortConnectionReference(this)
//    }
//
//    override fun getHoldPsiNode(): PortIdentifierPsiNode? {
//        return PsiTreeUtil
//            .findChildOfType(this, PortIdentifierPsiNode::class.java)
//    }
//
//    override fun getHoldPsiNodeRelativeTextRange(): TextRange? {
//        return getHoldPsiNode()
//            ?.textRange
//            ?.shiftLeft(this.textOffset)
//    }
//
//}
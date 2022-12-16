package top.criwits.scaleda.verilog.psi.factory.nodes

//import top.criwits.scaleda.verilog.psi.ScopeNode
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.psi.ScopeNode
import top.criwits.scaleda.verilog.parser.VerilogLexer
import top.criwits.scaleda.verilog.psi.factory.VerilogPsiLeafNodeFactory

class ModuleDeclarationPsiNode(node: ASTNode) : ANTLRPsiNode(node), PsiNameIdentifierOwner, ScopeNode {
    override fun resolve(element: PsiNamedElement?): PsiElement? {
        TODO("Not yet implemented")
    }

    override fun getNameIdentifier(): ModuleIdentifierPsiNode? {
        return PsiTreeUtil
            .findChildOfType(this, ModuleIdentifierPsiNode::class.java)
    }

    override fun getName(): String? {
        return nameIdentifier?.text
    }

    override fun getTextOffset(): Int {
        return nameIdentifier?.startOffsetInParent ?: 0
    }

    @Throws(IncorrectOperationException::class)
    override fun setName(s: String): PsiElement? {
        return PsiTreeUtil
            .findChildOfType(this.nameIdentifier, SimpleIdentifierPsiLeafNode::class.java)
            ?.replace(VerilogPsiLeafNodeFactory.create(VerilogLexer.Simple_identifier, s))
    }

//    override fun getAvailableNamedElements(): Collection<PsiNamedElement> {
//        return listOf(
//            PsiTreeUtil.findChildrenOfType(this, NetIdentifierPsiNode::class.java),
//            PsiTreeUtil.findChildrenOfType(this, ParameterIdentifierPsiNode::class.java),
//            PsiTreeUtil.findChildrenOfType(this, PortIdentifierPsiNode::class.java)
//                .filter {
//                    PsiTreeUtil.getParentOfType(it, ModuleInstantiationPsiNode::class.java) == null
//                },
//            PsiTreeUtil.findChildrenOfType(this, VariableIdentifierPsiNode::class.java)
//        )
//            .flatten()
//            .filterIsInstance(PsiNamedElement::class.java)
//    }
}

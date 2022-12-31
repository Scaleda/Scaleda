package top.criwits.scaleda.verilog.psi.factory.nodes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.criwits.scaleda.idea.verilog.VerilogLanguage$;
import top.criwits.scaleda.verilog.parser.VerilogLexer;

public class IdentifierPsiNode extends ANTLRPsiNode implements PsiNameIdentifierOwner {

    public IdentifierPsiNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        if (getNameIdentifier() == null)
            return null;
        return getNameIdentifier().getText();
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        // we are sure that RULE_identifier always has only one
        // leaf child which is either Simple_identifier or Escaped_identifier
        return this.getNode().getFirstChildNode().getPsi();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        ASTNode currentChild = this.getNode().getFirstChildNode();
        ASTNode newChild = new ANTLRPsiLeafNode(
                PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage$.MODULE$)
                        .get(VerilogLexer.Simple_identifier),
                name
        );
        this.getNode().replaceChild(currentChild, newChild);
        return this;
    }
}

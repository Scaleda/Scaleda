package top.scaleda.verilog.psi.nodes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.scaleda.verilog.VerilogLanguage$;
import top.scaleda.verilog.parser.VerilogLexer;

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
        try {
            return this.getNode().getFirstChildNode().getPsi();
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        ASTNode currentChild = this.getNode().getFirstChildNode();
        ASTNode newChild = new ANTLRPsiLeafNode(
                PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage$.MODULE$.INSTANCE())
                        .get(VerilogLexer.Simple_identifier),
                name
        );
        this.getNode().replaceChild(currentChild, newChild);
        return this;
    }
}

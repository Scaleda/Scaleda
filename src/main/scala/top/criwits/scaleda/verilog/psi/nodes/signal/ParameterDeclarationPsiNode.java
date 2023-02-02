package top.criwits.scaleda.verilog.psi.nodes.signal;

import com.intellij.lang.ASTNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import top.criwits.scaleda.verilog.psi.nodes.TypedDeclaration;

public class ParameterDeclarationPsiNode extends ANTLRPsiNode
        implements TypedDeclaration {

    public ParameterDeclarationPsiNode(@NotNull ASTNode node) {
        super(node);
    }

    public String getTypeText() {
        return getFirstChild().getFirstChild().getText();
    }

}

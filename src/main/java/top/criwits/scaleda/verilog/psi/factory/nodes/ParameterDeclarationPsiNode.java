package top.criwits.scaleda.verilog.psi.factory.nodes;

import com.intellij.lang.ASTNode;
import top.criwits.scaleda.verilog.psi.TypedDeclaration;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class ParameterDeclarationPsiNode extends ANTLRPsiNode
        implements TypedDeclaration {

    public ParameterDeclarationPsiNode(@NotNull ASTNode node) {
        super(node);
    }

    public String getTypeText() {
        return getFirstChild().getFirstChild().getText();
    }

}

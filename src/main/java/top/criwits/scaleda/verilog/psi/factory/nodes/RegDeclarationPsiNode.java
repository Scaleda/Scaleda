package top.criwits.scaleda.verilog.psi.factory.nodes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import top.criwits.scaleda.verilog.psi.TypedDeclaration;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RegDeclarationPsiNode extends ANTLRPsiNode
        implements TypedDeclaration {

    public RegDeclarationPsiNode(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * in reg_declaration all children except two last children is about type,
     * e.g. "reg signed [15:0] REG_NAME;", so we will pass it to typeText
     */
    @Override
    public String getTypeText() {
        PsiElement[] typeChildren = Arrays.copyOf(
                this.getChildren(),
                this.getChildren().length - 2
        );
        return String.join(" ",
                Arrays.stream(typeChildren)
                        .map(PsiElement::getText)
                        .collect(Collectors.toList())
        );
    }

}

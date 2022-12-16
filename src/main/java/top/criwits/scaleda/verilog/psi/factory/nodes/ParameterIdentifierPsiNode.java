package top.criwits.scaleda.verilog.psi.factory.nodes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParameterIdentifierPsiNode extends ANTLRPsiNode
        implements PsiNameIdentifierOwner {

    public ParameterIdentifierPsiNode(@NotNull ASTNode node) {
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
        return getFirstChild().getFirstChild();
    }

    @Override
    public PsiElement setName(@NotNull String s) throws IncorrectOperationException {
        return null;
    }
}

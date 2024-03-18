package top.scaleda.systemverilog.psi.nodes.module;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.scaleda.verilog.parser.VerilogLexer;
import top.scaleda.verilog.psi.VerilogPsiLeafNodeFactory;

public class ModuleIdentifierPsiNode extends ANTLRPsiNode implements PsiNameIdentifierOwner {

    public ModuleIdentifierPsiNode(@NotNull ASTNode node) {
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
        return getFirstChild().replace(VerilogPsiLeafNodeFactory.create(VerilogLexer.Simple_identifier, s));
    }
}

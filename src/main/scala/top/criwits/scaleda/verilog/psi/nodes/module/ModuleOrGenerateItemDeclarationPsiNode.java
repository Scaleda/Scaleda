package top.criwits.scaleda.verilog.psi.nodes.module;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import top.criwits.scaleda.verilog.psi.nodes.TypedDeclaration;
import top.criwits.scaleda.verilog.psi.nodes.signal.VariableDeclarationPsiNode;

public class ModuleOrGenerateItemDeclarationPsiNode
        extends ANTLRPsiNode
        implements TypedDeclaration {

    public ModuleOrGenerateItemDeclarationPsiNode(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * By default we consider that declaration has its type in first child's text.
     * This may be overridden by implementing TypedDeclaration by child class,
     * e.g. see {@link VariableDeclarationPsiNode}
     */
    @Override
    public String getTypeText() {
        // We are sure that there is always only one non-leaf child
        PsiElement unwrappedDeclaration = this.getFirstChild();

        if (unwrappedDeclaration instanceof TypedDeclaration) {
            return ((TypedDeclaration) unwrappedDeclaration).getTypeText();
        }

        return unwrappedDeclaration.getFirstChild().getText();
    }

}

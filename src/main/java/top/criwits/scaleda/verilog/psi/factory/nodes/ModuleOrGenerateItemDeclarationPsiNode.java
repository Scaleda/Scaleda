package top.criwits.scaleda.verilog.psi.factory.nodes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import top.criwits.scaleda.verilog.psi.TypedDeclaration;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class ModuleOrGenerateItemDeclarationPsiNode
        extends ANTLRPsiNode
        implements TypedDeclaration {

    public ModuleOrGenerateItemDeclarationPsiNode(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * By default we consider that declaration has its type in first child's text.
     * This may be overridden by implementing TypedDeclaration by child class,
     * e.g. see {@link RegDeclarationPsiNode}
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

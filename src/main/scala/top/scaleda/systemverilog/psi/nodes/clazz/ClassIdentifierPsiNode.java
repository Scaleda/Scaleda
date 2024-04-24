package top.scaleda.systemverilog.psi.nodes.clazz;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.scaleda.systemverilog.parser.SystemVerilogLexer;
import top.scaleda.systemverilog.psi.SystemVerilogPsiLeafNodeFactory;

public class ClassIdentifierPsiNode extends ANTLRPsiNode implements PsiNameIdentifierOwner {

  public ClassIdentifierPsiNode(@NotNull ASTNode node) {
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
    return getFirstChild().replace(SystemVerilogPsiLeafNodeFactory.create(SystemVerilogLexer.SIMPLE_IDENTIFIER, s));
  }
}
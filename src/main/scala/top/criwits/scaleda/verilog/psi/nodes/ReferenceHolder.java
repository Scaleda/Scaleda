package top.criwits.scaleda.verilog.psi.nodes;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

public interface ReferenceHolder<T extends PsiElement> {
    T getHoldPsiNode();
    TextRange getHoldPsiNodeRelativeTextRange();
}
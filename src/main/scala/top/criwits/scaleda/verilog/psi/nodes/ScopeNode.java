package top.criwits.scaleda.verilog.psi.nodes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.Collection;

public interface ScopeNode extends PsiElement {

    Collection<PsiNamedElement> getAvailableNamedElements();

}

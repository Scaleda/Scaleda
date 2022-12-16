package top.criwits.scaleda.verilog.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.Collection;

public interface ScopeNode extends PsiElement {

    Collection<PsiNamedElement> getAvailableNamedElements();

}

package top.scaleda.verilog.psi.nodes;

import com.intellij.psi.PsiElement;

/**
 * Used in case when reference has some type
 * (e.g. "input", "parameter", "wire" for Verilog).
 * <p>
 * Recommended usage is when {@link TypedDeclaration}
 * is a parent of corresponding {@link com.intellij.psi.PsiNamedElement},
 * so when type string is needed it can be found by calling
 * {@link com.intellij.psi.util.PsiTreeUtil#getParentOfType(PsiElement, Class)},
 * with {@link TypedDeclaration} as {@code Class}.
 */
public interface TypedDeclaration extends PsiElement {

    String getTypeText();

}

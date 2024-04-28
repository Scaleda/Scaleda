package top.scaleda.bluespec.psi.nodes

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import java.util

trait ScopeNode extends PsiElement { def getAvailableNamedElements: util.Collection[PsiNamedElement] }

package top.scaleda.bluespec.psi.nodes

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

trait ReferenceHolder[T <: PsiElement] {
  def getHoldPsiNode: T
  def getHoldPsiNodeRelativeTextRange: TextRange
}

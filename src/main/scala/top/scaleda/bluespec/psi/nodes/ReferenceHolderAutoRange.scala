package top.scaleda
package bluespec.psi.nodes

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

trait ReferenceHolderAutoRange[T <: PsiElement] {
  def getHoldPsiNode: T
  def getHoldPsiNodeRelativeTextRange: TextRange = getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)
  def getTextOffset: Int
}

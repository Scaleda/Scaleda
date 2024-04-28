package top.scaleda
package bluespec.psi.nodes

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

trait ReferenceHolderAutoRange[T <: PsiElement] {
  def getHoldPsiNode: T
  def getHoldPsiNodeRelativeTextRange: TextRange =
    if (getHoldPsiNode != null) getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset) else null
  def getTextOffset: Int
}

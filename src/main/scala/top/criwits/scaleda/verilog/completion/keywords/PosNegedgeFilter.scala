package top.criwits.scaleda
package verilog.completion.keywords

import verilog.psi.nodes.always.EventPrimaryPsiNode

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil

class PosNegedgeFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    if (context == null) return false
    val event = PsiTreeUtil.getParentOfType(context, classOf[EventPrimaryPsiNode])
    if (event == null) return false
    val firstChild = event.getFirstChild
    if (firstChild == null) return false
    if (!(firstChild.textMatches("posedge") || firstChild.textMatches("negedge")) && event.getChildren.length == 1)
      return true
    else return false

  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}

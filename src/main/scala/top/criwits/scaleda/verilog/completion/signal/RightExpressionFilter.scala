package top.criwits.scaleda
package verilog.completion.signal

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode

class RightExpressionFilter extends ElementFilter{
  override def isAcceptable(element: Any, context: PsiElement): Boolean = PsiTreeUtil.getParentOfType(context, classOf[ExpressionPsiNode]) != null

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}

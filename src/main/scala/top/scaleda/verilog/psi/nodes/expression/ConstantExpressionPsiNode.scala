package top.scaleda
package verilog.psi.nodes.expression

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ConstantExpressionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with IntEvaluable {
  def getExpression: Option[ExpressionPsiNode] = Option(PsiTreeUtil.getChildOfType(this, classOf[ExpressionPsiNode]))

  override def evaluate: Option[Int] = getExpression.flatMap(_.evaluate)
}

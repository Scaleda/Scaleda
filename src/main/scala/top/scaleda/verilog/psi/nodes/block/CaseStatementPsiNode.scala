package top.scaleda
package verilog.psi.nodes.block

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode

class CaseStatementPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getExpression: Option[ExpressionPsiNode] = {
    val child = PsiTreeUtil.findChildOfType(this, classOf[ExpressionPsiNode])
    Option(child)
  }
}

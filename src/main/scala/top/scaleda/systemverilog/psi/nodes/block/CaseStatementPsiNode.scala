package top.scaleda
package systemverilog.psi.nodes.block

import systemverilog.psi.nodes.expression.ExpressionPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class CaseStatementPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getExpression: Option[ExpressionPsiNode] = {
    val child = PsiTreeUtil.findChildOfType(this, classOf[ExpressionPsiNode])
    Option(child)
  }
}

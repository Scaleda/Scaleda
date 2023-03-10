package top.criwits.scaleda
package verilog.psi.nodes.instantiation

import verilog.psi.nodes.expression.ExpressionPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class OrderedPortConnectionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getSignal: ExpressionPsiNode = {
    PsiTreeUtil.getChildOfType(this, classOf[ExpressionPsiNode])
  }
}

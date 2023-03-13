package top.criwits.scaleda
package verilog.psi.nodes.signal

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode

class RangeDimensionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getFirstExpression: ExpressionPsiNode = {
    ???
  }

  def getSecondExpression: ExpressionPsiNode = {
    ???
  }
}

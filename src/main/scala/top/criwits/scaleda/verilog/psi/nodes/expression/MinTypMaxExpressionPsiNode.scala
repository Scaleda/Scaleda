package top.criwits.scaleda
package verilog.psi.nodes.expression

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class MinTypMaxExpressionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with IntEvaluable{
  override def evaluate: Option[Int] = {
    // If and only if this has 1 expression children, return it's value; otherwise return None
    None // TODO
  }
}

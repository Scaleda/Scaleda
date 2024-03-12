package top.scaleda
package verilog.psi.nodes.condition

import com.intellij.lang.ASTNode

class ConditionalStatementChainPsiNode(node: ASTNode) extends ConditionalStatementInnerPsiNode(node) {
  override def getElementName = "else if (...)"
}

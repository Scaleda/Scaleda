package top.scaleda
package verilog.psi.nodes.condition

import com.intellij.lang.ASTNode

class ConditionalStatementHeadPsiNode(node: ASTNode) extends ConditionalStatementInnerPsiNode(node) {
  override def getElementName = "if (...)"
}

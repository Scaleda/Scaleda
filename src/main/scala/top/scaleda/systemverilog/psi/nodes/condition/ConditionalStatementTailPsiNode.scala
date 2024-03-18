package top.scaleda
package systemverilog.psi.nodes.condition

import com.intellij.lang.ASTNode

class ConditionalStatementTailPsiNode(node: ASTNode) extends ConditionalStatementInnerPsiNode(node) {
  override def getElementName = "else (...)"
}

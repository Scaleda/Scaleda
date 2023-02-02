package top.criwits.scaleda
package verilog.psi.nodes.signal

import com.intellij.lang.ASTNode

class ParameterDeclarationPsiNode(node: ASTNode) extends SignalDeclarationPsiNode(node) {
  override def getTypeText: String = getFirstChild.getFirstChild.getText
}

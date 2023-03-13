package top.criwits.scaleda
package verilog.psi.nodes.always

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class DelayValuePsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getValue: Int = {
    this.getText.toInt
  }
}

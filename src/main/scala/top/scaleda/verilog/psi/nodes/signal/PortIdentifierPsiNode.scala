package top.scaleda
package verilog.psi.nodes.signal

import com.intellij.lang.ASTNode
import top.scaleda.verilog.psi.nodes.signal.PortDeclarationPsiNode.PortType

class PortIdentifierPsiNode(node: ASTNode) extends SignalIdentifierPsiNode(node) {
  def getPortType: PortType = {
    val parent = getDeclaration.asInstanceOf[PortDeclarationPsiNode]
    if (parent == null) return null // todo: ?
    parent.getPortType
  }
}

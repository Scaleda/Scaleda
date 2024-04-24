package top.scaleda
package systemverilog.psi.nodes.signal

import systemverilog.psi.nodes.signal.PortDeclarationPsiNode.PortType

import com.intellij.lang.ASTNode

class PortIdentifierPsiNode(node: ASTNode) extends SignalIdentifierPsiNode(node) {
  def getPortType: PortType = {
    val parent = getDeclaration.asInstanceOf[PortDeclarationPsiNode]
    if (parent == null) return null // todo: ?
    parent.getPortType
  }
}

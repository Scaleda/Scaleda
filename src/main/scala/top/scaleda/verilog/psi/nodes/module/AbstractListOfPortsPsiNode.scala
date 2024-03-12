package top.scaleda
package verilog.psi.nodes.module

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}

abstract class AbstractListOfPortsPsiNode(node: ASTNode)  extends  ANTLRPsiNode(node) {
  def getPortIdentifiers: Seq[PortIdentifierPsiNode]
}

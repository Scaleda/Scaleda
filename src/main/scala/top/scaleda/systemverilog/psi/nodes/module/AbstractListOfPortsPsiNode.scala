package top.scaleda
package systemverilog.psi.nodes.module

import systemverilog.psi.nodes.signal.PortIdentifierPsiNode

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

abstract class AbstractListOfPortsPsiNode(node: ASTNode)  extends  ANTLRPsiNode(node) {
  def getPortIdentifiers: Seq[PortIdentifierPsiNode]
}

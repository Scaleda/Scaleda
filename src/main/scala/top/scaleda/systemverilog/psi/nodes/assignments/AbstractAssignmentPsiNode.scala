package top.scaleda
package systemverilog.psi.nodes.assignments

import systemverilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

abstract class AbstractAssignmentPsiNode(node: ASTNode) extends ANTLRPsiNode(node){
  def getLValues: Iterable[SignalIdentifierPsiNode]
}

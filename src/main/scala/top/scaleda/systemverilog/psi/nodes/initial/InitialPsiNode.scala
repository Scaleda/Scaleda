package top.scaleda
package systemverilog.psi.nodes.initial

import systemverilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class InitialPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  override def getElementName: String = "initial"
}

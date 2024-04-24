package top.scaleda
package systemverilog.psi.nodes

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class StructureAllTextViewNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {

  override def getElementName: String = node.getText
}

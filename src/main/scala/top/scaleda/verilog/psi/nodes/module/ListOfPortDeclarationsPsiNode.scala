package top.scaleda
package verilog.psi.nodes.module

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}
import top.scaleda.verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}

/**
 * PSI node for list_of_port_declarations, ie 'input a, b, output [2:0] c'
 * @param node
 */
class ListOfPortDeclarationsPsiNode(node: ASTNode) extends AbstractListOfPortsPsiNode(node) {
  // get a, b and c
  override def getPortIdentifiers: Seq[PortIdentifierPsiNode] = {
    getPortDeclarations
      .flatMap(_.getDeclaration)
      .flatMap(_.getListOfPortIdentifiers)
      .flatMap(_.getPortIdentifiers)
  }

  // get 'input a, b' and 'output [2:0] c'
  def getPortDeclarations: Seq[PortDeclarationPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[PortDeclarationPsiNode])
    if (result == null) Seq[PortDeclarationPsiNode]() else result.toSeq
  }
}

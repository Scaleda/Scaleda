package top.scaleda
package verilog.psi.nodes.module

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}
import top.scaleda.verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}

class ListOfPortDeclarationsPsiNode(node: ASTNode) extends AbstractListOfPortsPsiNode(node) {
  override def getPortIdentifiers: Seq[PortIdentifierPsiNode] = {
    getPortDeclarations
      .flatMap(_.getDeclaration)
      .flatMap(_.getListOfPortIdentifiers)
      .flatMap(_.getPortIdentifiers)
  }

  def getPortDeclarations: Seq[PortDeclarationPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[PortDeclarationPsiNode])
    if (result == null) Seq[PortDeclarationPsiNode]() else result.toSeq
  }
}

package top.criwits.scaleda
package verilog.psi.nodes.module

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}

class ListOfPortDeclarationsPsiNode(node: ASTNode) extends AbstractListOfPortsPsiNode(node) {
  override def getPortIdentifiers: Seq[PortIdentifierPsiNode] = {
    PsiTreeUtil.getChildrenOfType(this, classOf[PortIdentifierPsiNode]).toSeq
  }
}

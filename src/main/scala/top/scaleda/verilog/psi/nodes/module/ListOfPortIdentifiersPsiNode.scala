package top.scaleda
package verilog.psi.nodes.module

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.signal.PortIdentifierPsiNode

abstract class AbstractListOfPortIdentifiersPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getPortIdentifiers: Seq[PortIdentifierPsiNode] ={
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[PortIdentifierPsiNode])
    if (result == null) Seq[PortIdentifierPsiNode]() else result.toSeq
  }
}

class ListOfPortIdentifiersPsiNode(node: ASTNode) extends AbstractListOfPortIdentifiersPsiNode(node)
class ListOfVariablePortIdentifiersPsiNode(node: ASTNode) extends AbstractListOfPortIdentifiersPsiNode(node)



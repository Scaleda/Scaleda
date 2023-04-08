package top.criwits.scaleda
package verilog.psi.nodes.signal

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.nodes.module.AbstractListOfPortIdentifiersPsiNode

abstract class AbstractDeclarationPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getListOfPortIdentifiers: Option[AbstractListOfPortIdentifiersPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[AbstractListOfPortIdentifiersPsiNode]))
  }
}

class OutputDeclarationPsiNode(node: ASTNode) extends AbstractDeclarationPsiNode(node)
class InputDeclarationPsiNode(node: ASTNode) extends AbstractDeclarationPsiNode(node)
class InoutDeclarationPsiNode(node: ASTNode) extends AbstractDeclarationPsiNode(node)

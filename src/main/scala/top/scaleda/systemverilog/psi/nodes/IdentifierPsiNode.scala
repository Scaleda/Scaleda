package top.scaleda
package systemverilog.psi.nodes

import systemverilog.parser.SystemVerilogLexer

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.systemverilog.psi.SystemVerilogPsiLeafNodeFactory


class IdentifierPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with PsiNameIdentifierOwner {
  override def getName: String = {
    if (getNameIdentifier == null) return null
    getNameIdentifier.getText
  }

  override def getNameIdentifier = getFirstChild.getFirstChild

  @throws[IncorrectOperationException]
  override def setName(s: String) = getFirstChild.replace(SystemVerilogPsiLeafNodeFactory.create(SystemVerilogLexer.IDENTIFIER, s))
}

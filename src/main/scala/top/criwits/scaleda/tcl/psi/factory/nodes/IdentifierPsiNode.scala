package top.criwits.scaleda
package tcl.psi.factory.nodes

import verilog.parser.VerilogLexer
import verilog.psi.factory.VerilogPsiLeafNodeFactory

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.tcl.parser.TclParser


class IdentifierPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with PsiNameIdentifierOwner {
  override def getName: String = {
    if (getNameIdentifier == null) return null
    getNameIdentifier.getText
  }

  override def getNameIdentifier = getFirstChild.getFirstChild

  @throws[IncorrectOperationException]
  override def setName(s: String) = getFirstChild.replace(VerilogPsiLeafNodeFactory.create(TclParser.IDENTIFICADOR, s))
}

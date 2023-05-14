package top.scaleda
package tcl.psi.factory.nodes

import tcl.parser.TclParser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode


class IdentifierPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with PsiNameIdentifierOwner {
  override def getName: String = {
    if (getNameIdentifier == null) return null
    getNameIdentifier.getText
  }

  override def getNameIdentifier = getFirstChild.getFirstChild

  @throws[IncorrectOperationException]
  override def setName(s: String) = getFirstChild.replace(TclPsiLeafNodeFactory.create(TclParser.IDENTIFICADOR, s))
}

package top.scaleda
package bluespec.psi.nodes

import bluespec.psi
import bluespec.parser.BluespecLexer

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
  override def setName(s: String) =
    getFirstChild.replace(psi.BluespecPsiLeafNodeFactory.create(BluespecLexer.Identifier, s))
}

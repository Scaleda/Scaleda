package top.scaleda
package bluespec.psi.nodes

import bluespec.parser.BluespecLexer
import bluespec.psi.BluespecPsiLeafNodeFactory

import com.intellij.lang.ASTNode
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.annotations.{NotNull, Nullable}

class SimpleIdentifierOwnerPsiNode(@NotNull node: ASTNode) extends ANTLRPsiNode(node) with PsiNameIdentifierOwner {
  override def getName: String = {
    if (getNameIdentifier == null) return null
    getNameIdentifier.getText
  }
  @Nullable override def getNameIdentifier: PsiElement = getFirstChild.getFirstChild
  @throws[IncorrectOperationException]
  override def setName(@NotNull s: String): PsiElement =
    getFirstChild.replace(BluespecPsiLeafNodeFactory.create(BluespecLexer.Identifier, s))
}

package top.scaleda
package systemverilog.psi.nodes.parameter

import systemverilog.parser.SystemVerilogLexer
import systemverilog.psi.SystemVerilogPsiLeafNodeFactory
import systemverilog.psi.nodes.{IdentifierPsiNode, StructureAllTextViewNode}

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}

class ParameterPortDeclarationPsiNode(node: ASTNode)
    extends StructureAllTextViewNode(node)
    with PsiNameIdentifierOwner {
  override def getNameIdentifier: PsiElement = PsiTreeUtil
    .findChildOfType(this, classOf[IdentifierPsiNode])

  override def setName(name: String): PsiElement = {
    val child = getNameIdentifier
    if (child == null) null
    else {
      child.replace(SystemVerilogPsiLeafNodeFactory.create(SystemVerilogLexer.SIMPLE_IDENTIFIER, name))
    }
  }
}

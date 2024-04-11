package top.scaleda
package systemverilog.psi.nodes.clazz

import systemverilog.parser.SystemVerilogLexer
import systemverilog.psi.SystemVerilogPsiLeafNodeFactory
import systemverilog.psi.nodes.IdentifierPsiNode
import systemverilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ClassPropertyPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with StructureViewNode
    with PsiNameIdentifierOwner {
  override def getElementName: String = node.getText.split(";").head

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

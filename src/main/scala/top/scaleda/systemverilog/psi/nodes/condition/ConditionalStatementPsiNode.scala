package top.scaleda
package systemverilog.psi.nodes.condition

import systemverilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ConditionalStatementPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with PsiElement with StructureViewNode {
  override def getElementName = "if-else"
}

abstract class ConditionalStatementInnerPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with PsiElement with StructureViewNode
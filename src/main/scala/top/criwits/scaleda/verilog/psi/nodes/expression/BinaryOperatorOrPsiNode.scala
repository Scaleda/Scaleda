package top.criwits.scaleda
package verilog.psi.nodes.expression

import verilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class BinaryOperatorOrPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with PsiElement with StructureViewNode {
  override def getElementName = "||"
}

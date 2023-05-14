package top.scaleda
package verilog.psi.nodes.incompleted

import verilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class IncompleteConditionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with PsiElement with StructureViewNode {
  override def getElementName: String = "IncompleteCondition"
}

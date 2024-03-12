package top.scaleda
package verilog.psi.nodes.instantiation

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode
import top.scaleda.verilog.psi.nodes.{DocumentHolder, ReferenceHolder}

abstract class AbstractPortConnectionPsiNode[T <: PsiElement](node: ASTNode)
  extends ANTLRPsiNode(node)
  with ReferenceHolder[T] {
  def getExpression: ExpressionPsiNode = {
    PsiTreeUtil.getChildOfType(this, classOf[ExpressionPsiNode])
  }
}

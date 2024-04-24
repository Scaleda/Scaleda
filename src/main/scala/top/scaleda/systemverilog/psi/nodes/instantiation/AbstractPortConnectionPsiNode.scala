package top.scaleda
package systemverilog.psi.nodes.instantiation

import systemverilog.psi.nodes.ReferenceHolder
import systemverilog.psi.nodes.expression.ExpressionPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

abstract class AbstractPortConnectionPsiNode[T <: PsiElement](node: ASTNode)
  extends ANTLRPsiNode(node)
  with ReferenceHolder[T] {
  def getExpression: ExpressionPsiNode = {
    PsiTreeUtil.getChildOfType(this, classOf[ExpressionPsiNode])
  }
}

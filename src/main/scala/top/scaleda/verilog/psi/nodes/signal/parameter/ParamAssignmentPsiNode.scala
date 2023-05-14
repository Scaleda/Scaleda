package top.scaleda
package verilog.psi.nodes.signal.parameter

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ParamAssignmentPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getParameterIdentifier: Option[ParameterIdentifierPsiNode] = Option(
    PsiTreeUtil.getChildOfType(this, classOf[ParameterIdentifierPsiNode])
  )
}

package top.scaleda
package systemverilog.psi.nodes.instantiation

import systemverilog.psi.nodes.ReferenceHolder
import systemverilog.references.NamedParameterAssignmentReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.systemverilog.psi.nodes.parameter.ParameterIdentifierPsiNode

class NamedParameterAssignmentPsiNode(node: ASTNode)
  extends ANTLRPsiNode(node)
  with ReferenceHolder[ParameterIdentifierPsiNode] {
  override def getHoldPsiNode: ParameterIdentifierPsiNode = PsiTreeUtil.getChildOfType(this, classOf[ParameterIdentifierPsiNode])

  override def getHoldPsiNodeRelativeTextRange: TextRange = if (getHoldPsiNode !=null) getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset) else null

  override def getReference = new NamedParameterAssignmentReference(this)
}

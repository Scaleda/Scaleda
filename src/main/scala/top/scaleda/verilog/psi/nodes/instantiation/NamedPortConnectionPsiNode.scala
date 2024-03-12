package top.scaleda
package verilog.psi.nodes.instantiation

import verilog.psi.nodes.ReferenceHolder
import verilog.references.NamedPortConnectionReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode
import top.scaleda.verilog.psi.nodes.signal.PortIdentifierPsiNode

class NamedPortConnectionPsiNode(node: ASTNode)
  extends AbstractPortConnectionPsiNode[PortIdentifierPsiNode](node) {
  override def getHoldPsiNode: PortIdentifierPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[PortIdentifierPsiNode])

  override def getHoldPsiNodeRelativeTextRange: TextRange =
    getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)

  override def getReference = new NamedPortConnectionReference(this)

}

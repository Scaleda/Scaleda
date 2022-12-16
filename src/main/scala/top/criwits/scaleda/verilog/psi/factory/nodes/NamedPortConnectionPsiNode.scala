package top.criwits.scaleda
package verilog.psi.factory.nodes

import verilog.references.NamedPortConnectionReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class NamedPortConnectionPsiNode(node: ASTNode)
  extends ANTLRPsiNode(node) with ReferenceHolder[PortIdentifierPsiNode] {
  override def getHoldPsiNode: PortIdentifierPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[PortIdentifierPsiNode])

  override def getHoldPsiNodeRelativeTextRange: TextRange =
    getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)

  override def getReference = new NamedPortConnectionReference(this)
}

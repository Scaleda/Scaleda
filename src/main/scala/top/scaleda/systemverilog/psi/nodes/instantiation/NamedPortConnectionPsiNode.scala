package top.scaleda
package systemverilog.psi.nodes.instantiation

import systemverilog.psi.nodes.signal.PortIdentifierPsiNode
import systemverilog.references.NamedPortConnectionReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil

class NamedPortConnectionPsiNode(node: ASTNode)
  extends AbstractPortConnectionPsiNode[PortIdentifierPsiNode](node) {
  override def getHoldPsiNode: PortIdentifierPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[PortIdentifierPsiNode])

  override def getHoldPsiNodeRelativeTextRange: TextRange =
    getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)

  override def getReference = new NamedPortConnectionReference(this)

}

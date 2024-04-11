package top.scaleda
package systemverilog.psi.nodes.instantiation

import systemverilog.psi.nodes.instantiation.ModuleInstantiationPsiNode._
import systemverilog.references.OrderedPortConnectionReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil

class OrderedPortConnectionPsiNode(node: ASTNode)
    extends AbstractPortConnectionPsiNode[OrderedPortConnectionPsiNode](node) {
  override def getHoldPsiNode: OrderedPortConnectionPsiNode = this

  override def getHoldPsiNodeRelativeTextRange: TextRange = this.getTextRange.shiftLeft(this.getTextOffset)

  override def getReference: OrderedPortConnectionReference = {
    // find parent ModuleInstantiationPsiNode
    val instant = PsiTreeUtil.getParentOfType(this, classOf[ModuleInstantiationPsiNode])
    val conn = instant.getConnectionType
    if (conn._1 != ORDERED) return null

    conn._2.get.zipWithIndex.find(_._1.asInstanceOf[OrderedPortConnectionPsiNode] == this) match {
      case Some((_, index)) => new OrderedPortConnectionReference(this, index)
      case None => null
    }
  }
}

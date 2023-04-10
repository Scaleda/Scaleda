package top.criwits.scaleda
package verilog.psi.nodes.module

import verilog.psi.nodes.signal.PortIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.nodes.ReferenceHolder
import top.criwits.scaleda.verilog.references.OldStylePortIdentifierReference

class PortPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getPortExpression: Option[PortExpressionPsiNode] = Option(
    PsiTreeUtil.getChildOfType(this, classOf[PortExpressionPsiNode])
  )
}

class PortExpressionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getPortReference: Option[PortReferencePsiNode] = Option(
    PsiTreeUtil.getChildOfType(this, classOf[PortReferencePsiNode])
  )
}

class PortReferencePsiNode(node: ASTNode) extends ANTLRPsiNode(node) with ReferenceHolder[PortIdentifierPsiNode] {
  // NOTICE: DO NOT DIRECTLY USE THESE IDENTIFIERS -- THEY MAY FUNCTION WRONGLY!!!
  def getPortIdentifier: Option[PortIdentifierPsiNode] = Option(
    PsiTreeUtil.getChildOfType(this, classOf[PortIdentifierPsiNode])
  )

  override def getName: String = getPortIdentifier.map(_.getName).orNull

  override def getHoldPsiNode: PortIdentifierPsiNode = getPortIdentifier.orNull

  override def getHoldPsiNodeRelativeTextRange: TextRange = getPortIdentifier.map(_.getTextRange.shiftLeft(this.getTextOffset)).orNull

  override def getReference = new OldStylePortIdentifierReference(this)
}
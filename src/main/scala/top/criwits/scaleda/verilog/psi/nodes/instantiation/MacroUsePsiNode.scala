package top.criwits.scaleda
package verilog.psi.nodes.instantiation

import verilog.psi.nodes.ReferenceHolder
import verilog.psi.nodes.`macro`.MacroIdentifierPsiNode
import verilog.references.MacroDefineReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class MacroUsePsiNode(node: ASTNode) extends ANTLRPsiNode(node) with ReferenceHolder[MacroIdentifierPsiNode] {
  override def getHoldPsiNode: MacroIdentifierPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[MacroIdentifierPsiNode])

  override def getReference = new MacroDefineReference(this)

  override def getHoldPsiNodeRelativeTextRange: TextRange = getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)

  override def getName: String = this.getHoldPsiNode().getName

}

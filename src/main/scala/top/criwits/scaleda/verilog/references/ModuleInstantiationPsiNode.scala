package top.criwits.scaleda
package verilog.references

import verilog.psi.factory.nodes.ReferenceHolder

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.factory.nodes.module.ModuleIdentifierPsiNode

class ModuleInstantiationPsiNode(node: ASTNode)
  extends ANTLRPsiNode(node) with ReferenceHolder[ModuleIdentifierPsiNode] {
  override def getHoldPsiNode: ModuleIdentifierPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[ModuleIdentifierPsiNode])

  override def getHoldPsiNodeRelativeTextRange: TextRange =
    getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)

  override def getReference = new ModuleInstantiationReference(this)
}

package top.criwits.scaleda
package verilog.psi.nodes.always

import verilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class AlwaysConstructPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  override def getElementName: String = {
    val delayOrEventControlPsiNode = PsiTreeUtil.findChildOfAnyType(this, classOf[DelayOrEventControlPsiNode])
    if (delayOrEventControlPsiNode == null) "always (unknown)" else "always " + delayOrEventControlPsiNode.getText
  }
}

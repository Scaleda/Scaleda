package top.criwits.scaleda
package verilog.psi.factory.nodes.always

import com.intellij.lang.ASTNode
import com.intellij.psi.util.{PsiTreeUtil, PsiUtil}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.{StructureViewNode, TypedDeclaration}

class AlwaysConstructPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  override def getElementName: String = {
    val delayOrEventControlPsiNode = PsiTreeUtil.findChildOfAnyType(this, classOf[DelayOrEventControlPsiNode])
    if (delayOrEventControlPsiNode == null) "(unknown)" else delayOrEventControlPsiNode.getText
  }
}

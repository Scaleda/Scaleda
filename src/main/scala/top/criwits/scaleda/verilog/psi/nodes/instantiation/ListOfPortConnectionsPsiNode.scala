package top.criwits.scaleda
package verilog.psi.nodes.instantiation

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ListOfPortConnectionsPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getNamedPorts: Array[NamedPortConnectionPsiNode] = {
    val r = PsiTreeUtil.getChildrenOfType(this, classOf[NamedPortConnectionPsiNode])
    if (r == null) Array.empty[NamedPortConnectionPsiNode] else r
  }

  def getOrderedPorts: Array[NamedPortConnectionPsiNode] = {
    val r = PsiTreeUtil.getChildrenOfType(this, classOf[NamedPortConnectionPsiNode])
    if (r == null) Array.empty[NamedPortConnectionPsiNode] else r
  }
}

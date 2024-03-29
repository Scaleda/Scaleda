package top.scaleda
package verilog.psi.nodes.instantiation

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ListOfPortConnectionsPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getNamedPorts: Seq[NamedPortConnectionPsiNode] = {
    val r = PsiTreeUtil.getChildrenOfType(this, classOf[NamedPortConnectionPsiNode])
    if (r == null) Seq[NamedPortConnectionPsiNode]() else r.toSeq
  }

  def getOrderedPorts: Seq[OrderedPortConnectionPsiNode] = {
    val r = PsiTreeUtil.getChildrenOfType(this, classOf[OrderedPortConnectionPsiNode])
    if (r == null) Seq[OrderedPortConnectionPsiNode]() else r.toSeq
  }
}

package top.scaleda
package verilog.psi.nodes.instantiation

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ListOfParameterAssignmentsPsiNode(node: ASTNode) extends ANTLRPsiNode(node){
  def getOrderedParameterAssignments: Seq[OrderedParameterAssignmentPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[OrderedParameterAssignmentPsiNode])
    if (result == null) Seq[OrderedParameterAssignmentPsiNode]() else result.toSeq
  }
  def getNamedParameterAssignments: Seq[NamedParameterAssignmentPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[NamedParameterAssignmentPsiNode])
    if (result == null) Seq[NamedParameterAssignmentPsiNode]() else result.toSeq
  }
}

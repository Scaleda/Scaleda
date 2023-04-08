package top.criwits.scaleda
package verilog.psi.nodes.signal.parameter

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ListOfParamAssignmentsPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getParamAssignments: Seq[ParameterAssignmentPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[ParameterAssignmentPsiNode])
    if (result == null) Seq[ParameterAssignmentPsiNode]() else result.toSeq
  }
}

package top.criwits.scaleda
package verilog.psi.nodes.signal.parameter

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ListOfParamAssignmentsPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getParamAssignments: Seq[ParamAssignmentPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[ParamAssignmentPsiNode])
    if (result == null) Seq[ParamAssignmentPsiNode]() else result.toSeq
  }
}

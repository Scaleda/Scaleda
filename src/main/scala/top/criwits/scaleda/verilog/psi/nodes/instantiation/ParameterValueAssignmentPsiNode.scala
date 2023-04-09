package top.criwits.scaleda
package verilog.psi.nodes.instantiation

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ParameterValueAssignmentPsiNode(node: ASTNode) extends ANTLRPsiNode(node){
  def getListOfParameterAssignments: Option[ListOfParameterAssignmentsPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[ListOfParameterAssignmentsPsiNode]))
  }
}

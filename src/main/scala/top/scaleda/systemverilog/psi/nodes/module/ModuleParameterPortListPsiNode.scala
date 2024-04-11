package top.scaleda
package systemverilog.psi.nodes.module

import systemverilog.psi.nodes.signal.parameter.{ParameterDeclarationPsiNode, ParameterIdentifierPsiNode}

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ModuleParameterPortListPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getParameterDeclarations: Seq[ParameterDeclarationPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[ParameterDeclarationPsiNode])
    if (result == null) Seq[ParameterDeclarationPsiNode]() else result.toSeq
  }

  def getParameterIdentifiers: Seq[ParameterIdentifierPsiNode] = {
    val declarations = getParameterDeclarations
    val r = declarations
      .flatMap(_.getListOfParamAssignments)
      .flatMap(_.getParamAssignments)
      .flatMap(_.getParameterIdentifier)
    r
  }
}

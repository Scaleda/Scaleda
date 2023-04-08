package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.psi.nodes.expression.{ConstantExpressionPsiNode, ExpressionPsiNode}

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

import com.intellij.lang.documentation.DocumentationMarkup._

class ParameterIdentifierPsiNode(node: ASTNode) extends SignalIdentifierPsiNode(node) {
  override def getDocument: String = {
    def getFullDefinition =
      s"${getTypeText} ${getName}${getInitialValue.map(" = " + _.getText).getOrElse("")}"
    s"${DEFINITION_START}${getFullDefinition}${DEFINITION_END}"
  }

  def getInitialValue: Option[ExpressionPsiNode] = {
    val paramAssign = PsiTreeUtil.getParentOfType(this, classOf[ParameterAssignmentPsiNode])
    if (paramAssign == null) return None

    val ce = PsiTreeUtil.getChildOfType(paramAssign, classOf[ConstantExpressionPsiNode])
    if (ce == null) return None

    Option(PsiTreeUtil.getChildOfType(ce, classOf[ExpressionPsiNode]))
  }

}

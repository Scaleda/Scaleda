package top.scaleda
package systemverilog.psi.nodes.signal.parameter

import systemverilog.psi.nodes.expression.{ConstantExpressionPsiNode, ExpressionPsiNode}
import systemverilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.lang.documentation.DocumentationMarkup._
import com.intellij.psi.util.PsiTreeUtil

class ParameterIdentifierPsiNode(node: ASTNode) extends SignalIdentifierPsiNode(node) {
  override def getDocument: String = {
    def getFullDefinition =
      s"${getTypeText} ${getName}${getInitialValue.map(" = " + _.getText).getOrElse("")}"
    s"${DEFINITION_START}${getFullDefinition}${DEFINITION_END}"
  }

  def getInitialValue: Option[ExpressionPsiNode] = {
    val paramAssign = PsiTreeUtil.getParentOfType(this, classOf[ParamAssignmentPsiNode])
    if (paramAssign == null) return None

    val ce = PsiTreeUtil.getChildOfType(paramAssign, classOf[ConstantExpressionPsiNode])
    if (ce == null) return None

    Option(PsiTreeUtil.getChildOfType(ce, classOf[ExpressionPsiNode]))
  }

}

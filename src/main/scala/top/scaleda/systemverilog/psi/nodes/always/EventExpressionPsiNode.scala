package top.scaleda
package systemverilog.psi.nodes.always

import systemverilog.psi.nodes.always.EventExpressionPsiNode.{LEVEL, NEGEDGE, POSEDGE}
import systemverilog.psi.nodes.expression.ExpressionPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode


class EventExpressionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getType: EventExpressionPsiNode.Value = {
    val children = this.getChildren
    children.foreach(child => {
      if (child.textMatches("posedge")) return POSEDGE
      if (child.textMatches("negedge")) return NEGEDGE
    })

    LEVEL
  }

  def getExpression: ExpressionPsiNode = {
    PsiTreeUtil.getChildOfType(this, classOf[ExpressionPsiNode])
  }
}

object EventExpressionPsiNode extends Enumeration {
  val POSEDGE, NEGEDGE, LEVEL = Value
}

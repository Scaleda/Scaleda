package top.scaleda
package verilog.psi.nodes.always

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.always.EventPrimaryPsiNode.{LEVEL, NEGEDGE, POSEDGE}
import top.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode


class EventPrimaryPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getType: EventPrimaryPsiNode.Value = {
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

object EventPrimaryPsiNode extends Enumeration {
  val POSEDGE, NEGEDGE, LEVEL = Value
}

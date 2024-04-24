package top.scaleda
package systemverilog.psi.nodes.signal

import systemverilog.psi.nodes.expression.ExpressionPsiNode

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

abstract class RangeDimensionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getPair: Option[(ExpressionPsiNode, ExpressionPsiNode)] = {
    val children = getChildren
    if (children.length != 5) return None
    Some(
      (
        // TODO: Buggy?
        children(1).getChildren.head.getChildren.head.asInstanceOf[ExpressionPsiNode],
        children(3).getChildren.head.getChildren.head.asInstanceOf[ExpressionPsiNode]
      )
    )
  }

  def generateText: String = {
    getPair match {
      case Some(v) => s"[${v._1.getText}:${v._2.getText}]"
      case _ => "[??:??]"
    }
  }
}

/**
 * RangePsiNode
 *
 * TODO: [??:??], or interface
 * @param node
 */
class RangeValuePsiNode(node: ASTNode) extends RangeDimensionPsiNode(node)
class CovergroupValuePsiNode(node: ASTNode) extends RangeDimensionPsiNode(node)

package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.psi.nodes.expression.ExpressionPsiNode

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

  override def toString: String = {
    getPair match {
      case Some(v) => s"[${v._1.getText}:${v._2.getText}]"
      case _ => "[??:??]"
    }
  }
}

class RangePsiNode(node: ASTNode) extends RangeDimensionPsiNode(node)
class DimensionPsiNode(node: ASTNode) extends RangeDimensionPsiNode(node)

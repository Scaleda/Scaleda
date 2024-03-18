package top.scaleda
package systemverilog.psi.nodes.expression

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class TermPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with IntEvaluable{
  override def evaluate: Option[Int] = {
    val children = getChildren
    if (children.isEmpty) return None
    val firstChild = children.head

    firstChild match {
      // Only primary
      case primary: PrimaryPsiNode => primary.evaluate
      // TODO: unary primary

      case _ => None
    }

  }
}
